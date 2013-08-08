package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.CheckResult
import fj.test.Property
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */
class BooleanTest {

	@Test
	void implies() {
		def p = Property.property(Arbitrary.arbInteger, {Integer a ->
			Bool.bool(a > 0).implies(true)
		} as F)
		def cr = p.check()
		cr.printlnSummary()
		assertTrue(cr.isOk())
	}


}
