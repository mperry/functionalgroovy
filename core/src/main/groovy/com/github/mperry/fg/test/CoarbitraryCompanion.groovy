package com.github.mperry.fg.test

import com.github.mperry.fg.ListJavaExtension
import fj.F
import fj.test.Coarbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 10/01/14.
 */
@TypeChecked
class CoarbitraryCompanion {

//    @TypeChecked(TypeCheckingMode.SKIP)
    static <A> Coarbitrary<java.util.List<A>> coarbJavaList(Coarbitrary<A> ca) {
        Coarbitrary.coarbList(ca).compose({ java.util.List list -> ListJavaExtension.toFJList(list) } as F)
    }

}
