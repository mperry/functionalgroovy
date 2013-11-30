package com.github.mperry.fg.test

import fj.test.Arbitrary
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
class IntegerOverflow {

	@Test
	void overflow() {
		PropertyTester.showAll (false) { a, b -> a >= 0 && b >= 0 } { Integer a, Integer b ->
			a + b >= 0
		}
	}

}
