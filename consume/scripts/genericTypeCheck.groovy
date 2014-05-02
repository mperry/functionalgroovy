
// does not type check with Groovy 2.3.0-rc-4

import groovy.transform.TypeChecked
//import org.junit.Test

@TypeChecked
class Class1 {

    static <A, B> void method1(A a, B b) {
        method2(a, b)
    }

    static <A, B> void method2(A a, B b) {
    }

    static <A, B> void method3(List<A> list1, List<B> list2) {
        method1(list1.get(0), list2.get(0))
    }

}

