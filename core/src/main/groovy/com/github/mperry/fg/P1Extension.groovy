package com.github.mperry.fg

import fj.F
import fj.P1
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/01/14.
 */
@TypeChecked
class P1Extension {

    static <A> Boolean throwsException(P1<A> p, Class error) {
        try {
            p._1()
            false
        } catch (Exception e) {
            ObjectExtension.isSubInstanceOf(e, error)
        }
    }

}
