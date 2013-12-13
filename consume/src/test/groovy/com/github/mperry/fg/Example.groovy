package com.github.mperry.fg

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/12/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
class Example {

    @Test
    void test1() {
        println "First FunctionalGroovy demo"
        1.to(5).each {
            println it
        }
    }

}
