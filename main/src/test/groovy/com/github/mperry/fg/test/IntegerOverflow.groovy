package com.github.mperry.fg.test

import fj.data.Option
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.data.Option.some

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
class IntegerOverflow {

	/**
	 * It is false, that given a and b is a natural, that a + b is a natural, due to overflow
	 */
	@Test
	void overflow() {
		showAll new PropertyConfig(
			truth: false,
			pre: some { a, b -> a >= 0 && b >= 0 },
			function: { Integer a, Integer b ->
				a + b >= 0
			}
		)
	}

}
