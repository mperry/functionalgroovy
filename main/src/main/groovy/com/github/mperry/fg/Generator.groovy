package com.github.mperry.fg

import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 25/02/13
 * Time: 11:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Canonical
@TypeChecked
class Generator {

	String name
	Closure func
	Boolean guard = false

}
