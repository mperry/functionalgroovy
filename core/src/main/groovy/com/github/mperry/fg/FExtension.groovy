package com.github.mperry.fg

import fj.F
import fj.F3

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 1/12/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
class FExtension {

	static <A, B> Closure<B> toClosure(F<A, B> f) {
		{ A a -> f.f(a) }
	}

}
