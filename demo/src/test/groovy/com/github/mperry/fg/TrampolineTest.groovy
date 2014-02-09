package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class TrampolineTest {

    @Test
    void test1() {
        def list = fj.data.List.list(1, 4, 6, 7)

        def val = list.foldRightC({ Integer a, Integer acc -> a + acc } as F2<Integer, Integer, Integer>, 0)
        println val.run()

    }


}
