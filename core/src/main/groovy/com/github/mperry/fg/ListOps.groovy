package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 26/06/2014.
 */
@TypeChecked
class ListOps {

	static <A> List<A> plus(List<A> list1, List<A> list2) {
		def result = new ArrayList<A>(list1)
		result.addAll(list2)
		result
	}

	static <A> List<A> plus(A a, List<A> list) {
		def result = new ArrayList<A>()
		result.add(a)
		result.addAll(list)
		result
	}

	static <A> List<A> plus(List<A> list, A a) {
		def result = new ArrayList<A>()
		result.addAll(list)
		result.add(a)
		result
	}

}
