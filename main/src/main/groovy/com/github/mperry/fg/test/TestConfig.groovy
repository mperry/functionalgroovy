package com.github.mperry.fg.test

import fj.F
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import groovy.transform.Canonical
import groovy.transform.Immutable

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 1/12/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Canonical
//@Immutable
class TestConfig {

	Map<Class<?>, Arbitrary> map = PropertyTester.defaultMap
	Closure<Boolean> function
	Option<Closure<Boolean>> pre = Option.none()
	Boolean truth = true
	F<Validation<Throwable, Boolean>, Boolean> valid = { Validation<Throwable, Boolean> v -> v.isSuccess() && v.success() == true } as F

	TestConfig addArbs(Map<Class<?>, Arbitrary> m) {
		new TestConfig(map + m, function, pre, truth)
	}

}

