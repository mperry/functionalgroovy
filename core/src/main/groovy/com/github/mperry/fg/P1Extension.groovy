package com.github.mperry.fg

import fj.F
import fj.P1
import fj.data.Option
import fj.data.Validation
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/01/14.
 */
@TypeChecked
class P1Extension {

    static <A> Boolean throwsException(P1<A> p, Class error) {
        def v = validate(p)
        v.isFail() && ObjectExtension.isSubInstanceOf(v.fail(), error)
    }

    static <A> Boolean throwsException(P1<A> p) {
        validate(p).isFail()
    }

    static <A> Validation<Exception, A> validate(P1<A> p) {
        try {
            Validation.success(p._1())
        } catch (Exception e) {
            Validation.fail(e)
        }
    }

    static <A> Boolean throwsError(P1<A> p, Class error) {
        def v = validateWithError(p)
        v.isFail() && ObjectExtension.isSubInstanceOf(v.fail(), error)
    }

    static <A> Boolean throwsStackOverflow(P1<A> p) {
        throwsError(p, StackOverflowError.class)
    }

    static <A> Boolean throwsError(P1<A> p) {
        validateWithError(p).isFail()
    }

    static <A> Validation<Error, A> validateWithError(P1<A> p) {
        try {
            Validation.success(p._1())
        } catch (Error e) {
            Validation.fail(e)
        }
    }


}
