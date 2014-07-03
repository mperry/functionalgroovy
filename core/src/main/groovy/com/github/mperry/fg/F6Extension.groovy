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

	static <A, B, C, D, E, $F, G> Closure<G> toClosure(F6<A, B, C, D, E, $F, G> func) {
		{ A a, B b, C c, D d, E e, $F f ->
			func.f(a, b, c, d, e, f)
		}
	}

}
