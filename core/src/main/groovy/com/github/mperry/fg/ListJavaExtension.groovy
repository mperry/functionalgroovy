package com.github.mperry.fg

import fj.F;
import fj.P;
import fj.P2;
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

    static <A, B> java.util.List<B> flatMap(java.util.List<A> list, F<A, List<B>> f) {
        def result = new ArrayList<B>()
        for (A a: list) {
            result.addAll(f.f(a))
        }
        result
    }


}
