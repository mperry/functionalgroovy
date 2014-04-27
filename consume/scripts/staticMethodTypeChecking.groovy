
// defect in Groovy 2.3.0-rc-1, code does not compile
// run with: groovy staticMethodTypeChecking.groovy

// raised http://jira.codehaus.org/browse/GROOVY-6723

import groovy.transform.TypeChecked

@TypeChecked
class Test1 {

    static <A, B> void pair1(A a, B b) {
    }

    static <A, B> void pair2(A a, B b) {
        pair1(a, a)
    }

    static <A> List<A> list1(A a) {
        [a]
    }

    static <B> List<B> list2(B b) {
        list1(b)
    }

    static <A> List<A> list3(A a) {
        list1(a)
    }

}
