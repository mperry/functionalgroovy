package com.github.mperry.fg.di

import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class UserRepository {

    Map<Integer, User> map = [1: new User(1, "ian", "ian@dummy.com"), 3: new User(3, "mark", "mark@dummy.com")]

    Option<User> get(Integer id) {
        Option.fromNull(map.get(id))
    }

    Option<User> find(String username) {
        Option.none()
    }

}
