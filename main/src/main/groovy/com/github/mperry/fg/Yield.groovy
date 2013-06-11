package com.github.mperry.fg

import groovy.transform.TypeChecked
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
@Canonical
@TypeChecked
class Yield {

	Map values = [:]
	Closure closure

	Object propertyMissing(String name) {
		values[name]
	}

}
