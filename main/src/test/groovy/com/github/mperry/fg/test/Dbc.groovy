package com.github.mperry.fg.test

import fj.test.Bool
import fj.test.CheckResult
import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Requires
import org.junit.Test

class Dbc extends GroovyTestCase {

	@Test
	void test1() {
		def pre = { true }
		def post = { true }
		def func = { true }
		if (pre()) {
			func()
		}
		def p = Bool.bool(pre()).implies(post())
		def cr = p.check()
		CheckResult.summary.println(cr)
		assertTrue(cr.isPassed() || cr.isProven())
	}

	@Requires({a > 0})
	@Ensures({ result > 0})
	int method1(int a) {
		2 * a
	}

}
