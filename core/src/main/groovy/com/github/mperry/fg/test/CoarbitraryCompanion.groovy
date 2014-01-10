package com.github.mperry.fg.test

import fj.F
import fj.test.Coarbitrary

/**
 * Created by MarkPerry on 10/01/14.
 */
class CoarbitraryCompanion {

    static <A> Coarbitrary<List<A>> coarbJavaList(Coarbitrary<A> ca) {
        Coarbitrary.coarbList(ca).compose({ List list -> list.toFJList() } as F)
    }

}
