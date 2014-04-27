
import groovy.transform.TypeChecked

@TypeChecked
class C1<A> {

    def void m1(A a) {
        C1.m2(a)
    }

    static <B> void m2(B b) {

    }

}
