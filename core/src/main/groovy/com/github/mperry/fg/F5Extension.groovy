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
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class F5Extension {

	static <A, B, C, D, E, $F> Closure<$F> toClosure(F5<A, B, C, D, E, $F> f) {
		{ A a, B b, C c, D d, E e ->
			f.f(a, b, c, d, e)
		}
	}

}
