package com.github.mperry.fg

import groovy.transform.TypeChecked
import org.junit.Test

import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 16/01/14.
 */
@TypeChecked
class ListTest {

    @Test
    void test1() {
        assertTrue(List.join([[1, 2], [3, 4], []]) == [1, 2, 3, 4])
    }

}
