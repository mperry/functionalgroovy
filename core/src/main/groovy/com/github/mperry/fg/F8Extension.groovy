package com.github.mperry.fg

import fj.F
import fj.F7
import fj.F8
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class F8Extension {

	static <A, B, C, D, E, $F, G, H, I> F<A, F<B, F<C, F<D, F<E, F<$F, F<G, F<H, I>>>>>>>> curry(F8<A, B, C, D, E, $F, G, H, I> f8) {
		{ a ->
			{ b ->
				{ c ->
					{ d ->
						{ e ->
							{ f ->
								{ g ->
									{ h ->
										f8.f(a, b, c, d, e, f, g, h)
									} as F
								} as F
							} as F
						} as F
					} as F
				} as F
			} as F
		} as F
	}
}
