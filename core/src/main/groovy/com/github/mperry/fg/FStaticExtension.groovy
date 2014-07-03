package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 20/12/13
 * Time: 8:45 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class FStaticExtension {

	public static <A, B> F<A, B> unit(F z, Closure<B> closure) {
		closure as F<A, B>
	}

}
