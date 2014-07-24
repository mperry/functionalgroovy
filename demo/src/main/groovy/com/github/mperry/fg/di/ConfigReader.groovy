package com.github.mperry.fg.di

import com.github.mperry.fg.ReaderM
import fj.data.Option
import groovy.transform.TypeChecked

import static com.github.mperry.fg.ReaderM.lift

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class ConfigReader {

    static ReaderM<Config, Option<User>> user(Integer id) {
        ReaderM.<Config, Option<User>>lift { Config c -> c.userRepository.get(id) }
    }

    static ReaderM<Config, Option<User>> user(String username) {
        ReaderM.<Config, Option<User>>lift { Config c -> c.userRepository.find(username) }
    }

    static ReaderM<Config, Option<String>> userEmail(Integer id) {
        user(id).map { Option<User> o -> o.map { User u -> u.email } }
    }

    static ReaderM<Config, String> mailService() {
        ReaderM.<Config, String>lift { Config c -> c.mailServer }
    }

}
