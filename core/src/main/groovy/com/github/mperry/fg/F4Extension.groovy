package com.github.mperry.fg

import fj.F
import fj.F4
import fj.F5
import fj.P1
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class F4Extension {

	static <A, B, C, D, E> F<A, F<B, F<C, F<D, E>>>> curry(F4<A, B, C, D, E> f4) {
		{ a ->
			{ b ->
				{ c ->
					{ d ->
						f4.f(a, b, c, d)
					} as F
				} as F
			} as F
		} as F
	}

    static <A, B, C, D, E> P1<E> f_(F4<A, B, C, D, E> f, A a, B b, C c, D d) {
        { -> f.f(a, b, c, d)} as P1
    }

}
