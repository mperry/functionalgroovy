package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 1/12/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class F2Extension {

	static <A, B, C> Closure<C> toClosure(F2<A, B, C> f) {
		{ A a, B b ->
			f.f(a, b)
		}
	}

}
