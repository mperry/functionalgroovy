package com.github.mperry.fg.test

import static PropertyTester.*

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
class PropTest1 extends GroovyTestCase {


	void test1() {
		showAll { Integer a, Integer b ->
			a + b == b + a
		}
//		assertTrue(true)
	}

}
