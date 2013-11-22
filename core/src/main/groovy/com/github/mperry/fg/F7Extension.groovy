package com.github.mperry.fg

import fj.F
import fj.F6
import fj.F7

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
class F7Extension {

	static <A, B, C, D, E, $F, G, H> F<A, F<B, F<C, F<D, F<E, F<$F, F<G, H>>>>>>> curry(F7<A, B, C, D, E, $F, G, H> f7) {
		{ a ->
			{ b ->
				{ c ->
					{ d ->
						{ e ->
							{ f ->
								{ g ->
									f7.f(a, b, c, d, e, f, g)
								} as F
							} as F
						} as F
					} as F
				} as F
			} as F
		} as F
	}

}
