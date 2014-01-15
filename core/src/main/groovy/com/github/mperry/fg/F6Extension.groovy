package com.github.mperry.fg

import fj.F
import fj.F5
import fj.F6
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
class F6Extension {

	static <A, B, C, D, E, $F, G> F<A, F<B, F<C, F<D, F<E, F<$F, G>>>>>> curry(F6<A, B, C, D, E, $F, G> f6) {
		{ a ->
			{ b ->
				{ c ->
					{ d ->
						{ e ->
							{ f ->
								f6.f(a, b, c, d, e, f)
							} as F
						} as F
					} as F
				} as F
			} as F
		} as F
	}

    static <A, B, C, D, E, $F, G> P1<G> f_(F6<A, B, C, D, E, $F, G> f6,
                                           A a, B b, C c, D d, E e, $F f) {
        { -> f6.f(a, b, c, d, e, f) } as P1
    }

}
