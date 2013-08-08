package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.Show
import fj.data.Stream
import fj.test.*
import org.gcontracts.ClassInvariantViolation
import org.gcontracts.PostconditionViolation
import org.gcontracts.PreconditionViolation
import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Requires
import org.junit.Test

import static fj.Show.anyShow
import static fj.Show.showS

class DbcTest extends GroovyTestCase {

	Integer sum(Integer a, Integer b) {
		a + b
	}

	@Requires({a >= 0 && b >= 0})
	@Ensures({ result >= 0})
	Integer sum2(Integer a, Integer b) {
		a + b
	}

	@Test
	void test6() {
		def v = new MethodVerifier().verify(this, "sum2")
		if (v.isFail()) {
			println v.fail()
		}
		v.map({ assertTrue(it.isPassed() || it.isProven())} as F)
	}




}
