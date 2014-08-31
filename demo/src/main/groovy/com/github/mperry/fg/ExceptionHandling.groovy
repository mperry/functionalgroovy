package com.github.mperry.fg

import fj.P1
import fj.data.Validation
import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by mperry on 4/08/2014.
 */
@TypeChecked
class ExceptionHandling {

    Validation<? extends IOException, String> contents(String s) {
        ({ -> (new URL(s)).text } as P1).validate()
    }

    @Test
    void errorTest() {
        def actual = ["malformed", "http://www.this-page-intentionally-left-blank.org/"].collect { String s ->
            contents(s).isSuccess()
        }
        assert actual == [false, true]
    }

}


