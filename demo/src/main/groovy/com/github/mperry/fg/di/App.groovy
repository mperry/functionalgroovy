package com.github.mperry.fg.di

import com.github.mperry.fg.ReaderM
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class App {

    Config config

    @TypeChecked(TypeCheckingMode.SKIP)
    Option<String> userEmail(Integer id) {
        run(ConfigReader.userEmail(id))
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    Option<User> user(Integer id) {
        run(ConfigReader.user(id))
    }

    String mailService() {
        run(ConfigReader.mailService())
    }

    def <A> A run(ReaderM<Config, A> reader) {
        reader.f(config)
    }

}
