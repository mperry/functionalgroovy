package com.github.mperry.fg

import fj.P
import fj.P2
import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 17/02/14.
 */
@TypeChecked
@Canonical
class Rand {

    Long seed

    P2<Integer, Rand> nextInt() {
        def newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
        def nextRng = new Rand(newSeed)
        def n = (Long) (newSeed >>> 16)
        def i = n.intValue()
        P.p(i, nextRng)
    }

    P2<Integer, Rand> nextNatural() {
        def p = nextInt()
        def i = p._1()
        P.p(i < 0 ? -(i + 1) : i, p._2())
    }

    P2<Double, Rand> nextDouble() {
        def p = nextNatural()
        P.p(p._1() / (Integer.MAX_VALUE.toDouble() + 1), p._2())
    }

    P2<Boolean, Rand> nextBoolean() {
        def p = nextInt()
        P.p(p._1() % 2 == 0, p._2())
    }



}
