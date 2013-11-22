package com.github.mperry.fg

import fj.F
import fj.F5

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
class F5Extension {

	static <A, B, C, D, E, $F> F<A, F<B, F<C, F<D, F<E, $F>>>>> curry(F5<A, B, C, D, E, $F> f5) {
		{ a ->
			{ b ->
				{ c ->
					{ d ->
						{ e ->
							f5.f(a, b, c, d, e)
						} as F
					} as F
				} as F
			} as F
		} as F
	}

}
