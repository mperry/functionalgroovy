package com.github.mperry.fg

import fj.F
import fj.F7
import fj.F8
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
class F8Extension {


	static <A, B, C, D, E, $F, G, H, I> Closure<I> toClosure(F8<A, B, C, D, E, $F, G, H, I> func) {
		{ A a, B b, C c, D d, E e, $F f, G g, H h ->
			func.f(a, b, c, d, e, f, g, h)
		}
	}


}
