package com.github.mperry.fg

import fj.F2
import groovy.transform.TypeChecked
import org.junit.Before
import org.junit.Test

import javax.management.relation.RoleList

import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 15/01/14.
 */
@TypeChecked
class ObjectTest {

    List<Integer> list

    @Before
    void setUp() {
        list = new ArrayList<>()
        list.addAll([3, 4])
    }

    def <A> void checkCompatibility(List<Class> classes, A o, F2<A, Class, Boolean> f, Boolean truth) {
        classes.each { Class clazz ->
            assertTrue(f.f(o, clazz) == truth)
        }
    }

    def <A> void checkCompatibility(List<Class> good, List<Class> bad, A a, F2<A, Class, Boolean> f) {
        checkCompatibility(good, a, f, true)
        checkCompatibility(bad, a, f, false)
    }

    @Test
    void subInstance() {
        def ok = [List.class, ArrayList.class, Collection.class]
        def bad = [RoleList.class]
        checkCompatibility(ok, bad, list, { Object o, Class c -> o.isSubInstanceOf(c)} as F2)
    }

    @Test
    void direct() {
        def ok = [ArrayList.class]
        def bad = [RoleList.class, List.class, Collection.class]
        checkCompatibility(ok, bad, list, { Object o, Class c -> o.isDirectInstanceOf(c)} as F2)
    }

    @Test
    void properSubInstance() {
        def ok = [List.class, Collection.class]
        def bad = [RoleList.class, ArrayList.class]
        checkCompatibility(ok, bad, list, { Object o, Class c -> o.isProperSubInstanceOf(c)} as F2)
    }

}
