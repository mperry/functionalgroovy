package com.github.mperry.fg

import fj.data.Option
import fj.data.Stream
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

	// left shift should only accept a Functor or Monad

	def leftShift(final Stream s) {
		func = { s }
	}

	def leftShift(final List list) {
		func = { list }
	}

	def leftShift(final Collection c) {
		func = { c }
	}

	def leftShift(final Option o) {
		func = { o }
	}

	def leftShift(final Closure c) {
		func = c
	}

	def leftShift(final Object o) {
		throw new UnsupportedOperationException("Comprehension not supported for object $o with class ${o.class.canonicalName}")
	}

}
