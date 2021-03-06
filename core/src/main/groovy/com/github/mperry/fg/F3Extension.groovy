package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.F4
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
class F3Extension {

	static <A, B, C, D> Closure<D> toClosure(F3<A, B, C, D> f) {
		{ A a, B b, C c ->
			f.f(a, b, c)
		}
	}


}
