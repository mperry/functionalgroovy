package com.github.mperry.fg.test

import fj.data.Validation
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 1/12/13
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
class MyTest {

	@Test
	void test1() {
		def f = { a, b -> a + b == b + a }
		def v  = perform(f, [2, 3])
		int z = 0
	}

	Validation<Throwable, Boolean> perform(Closure<Boolean> c, List args) {
		try {
			Validation.success(c.call(args))
		} catch (Throwable t) {
			Validation.fail(t)
		}
	}

}
