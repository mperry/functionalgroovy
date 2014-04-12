package com.github.mperry.fg

import fj.F
import fj.F2
import fj.P
import fj.P1;
import fj.P2
import fj.control.Trampoline;
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode;
//import groovy.transform.TypeChecked

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 1/12/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class ListJavaExtension {

	public static <A, B> List<P2<A, B>> zip(List<A> list1, List<B> list2) {
		def result = []
		def min = Math.min(list1.size(), list2.size())
		for (int i = 0; i < min; i++) {
			result.add(P.p(list1.get(i), list2.get(i)))
		}
		result
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	public static <A, B> java.util.List<B> map(java.util.List<A> list, F<A, B> f) {
        list.map(f.toClosure())
	}

	static <A, B> java.util.List<B> collect(java.util.List<A> list, F<A, B> f) {
		map(list, f)
	}

	static <A> fj.data.List<A> toFJList(List<A> list) {
        return fj.data.List.list((A[]) list.toArray());
    }

    static <A, B> B fold(List<A> list, B b, F2<B, A, B> f) {
        foldLeft(list, b, f)
    }

    static <A, B> B fold(List<A> list, B b, Closure<B> f) {
        foldLeft(list, b, f)
    }

//    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> B foldLeft(List<A> list, B b, F2<B, A, B> f) {
        def acc = b
        for (A a: list) {
            acc = f.f(acc, a)
        }
        acc
    }

    static <A, B> B foldLeft(List<A> list, B b, Closure<B> f) {
        foldLeft(list, b, f as F2)
    }

    /**
     * Fold left with recursion
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> B foldLeftR(List<A> list, B b, F2<B, A, B> f) {
        list.empty ? b : foldLeftR(list.tail(), f.f(b, list.head()), f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> B foldLeftR(List<A> list, B b, Closure<B> f) {
        list.empty ? b : foldLeftR(list.tail(), f.call(b, list.head()), f)
    }

    static <A, B> B foldRight(List<A> list, B b, F2<B, A, B> f) {
        foldRightT(list, b, f)
//        foldRightF(list, b, f)
    }

    static <A, B> B foldRight(List<A> list, B b, Closure<B> f) {
        foldRight(list, b, f as F2)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> B foldRightF(List<A> list, B b, F2<B, A, B> f) {
//        list.isEmpty() ? b : foldRightF(list.tail(), f.f(b, list.head()), f)
        list.isEmpty() ? b : f.f(foldRightF(list.tail(), b, f), list.head())
    }

    static <A, B> B foldRightT(List<A> list, B b, F2<B, A, B> f) {
        // Workaround, g is defined explicitly instead of using f.flip because
        // using f.flip causes a StackOverflowError
        // I did not look into what caused this error
        def g = { A a2, B b2 ->
            f.f(b2, a2)
        } as F2
        def t = foldRightTrampoline(list, b, g)
        t.run()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> Trampoline<B> foldRightTrampoline(List<A> list, B b, F2<A, B, B> f) {
        Trampoline.suspend({ ->
            if (list.empty) {
                Trampoline.pure(b)
            } else {
                def t = list.tail()
                def h = list.head()
                foldRightTrampoline(t, b, f).map(Functions.curry(f).f(h))
            }
        } as P1)
    }

    static <A, B> java.util.List<B> flatMap(java.util.List<A> list, F<A, List<B>> f) {
        def result = new LinkedList<B>()
        for (A a: list) {
            result.addAll(f.f(a))
        }
        result
    }


}
