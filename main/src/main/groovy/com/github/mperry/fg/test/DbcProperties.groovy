package com.github.mperry.fg.test

import fj.F2
import fj.test.Arbitrary
import fj.test.Property
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class DbcProperties {

	static Integer sum(Integer a, Integer b) {
		a + b
	}

	static Property test1Prop() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
//			println "$a $b"
			Property.prop(sum(a, b) == sum(b, a))
		} as F2)

	}

}
