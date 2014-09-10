package com.github.mperry

import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by mperry on 10/09/2014.
 */
@TypeChecked
class TestCoercion {

    @Test
    void test1() {

        callb(new B())

        def a1 = new A()
        B b1 = new B()
        B b2 = [a1]
        B b3 = new B(a1)

        callb(b1)

        callb(a1 as B)
        callb(b2)
        callb([a1] as B)

        callb(b3)


    }


    void callb(B b) {

    }

}
