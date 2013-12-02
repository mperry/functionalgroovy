package com.github.mperry.fg.test

import fj.F
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import groovy.transform.Canonical
import groovy.transform.Immutable
import groovy.transform.TypeChecked

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

	static final F<Validation<Throwable, Boolean>, Boolean> DEFAULT_VALIDATOR = { Validation<Throwable, Boolean> v ->
		v.isFail() ? false : v.success()
	} as F

	Map<Class<?>, Arbitrary> map = PropertyTester.defaultMap
	Closure<Boolean> function
	Option<Closure<Boolean>> pre = Option.none()
	Boolean truth = true
	F<Validation<Throwable, Boolean>, Boolean> validator = DEFAULT_VALIDATOR

	TestConfig addArbs(Map<Class<?>, Arbitrary> m) {
		new TestConfig(map + m, function, pre, truth)
	}

	@TypeChecked
	static F<Validation<Throwable, Boolean>, Boolean> validator(F<Throwable, Boolean> f) {
		{ Validation<Throwable, Boolean> v ->
			v.isFail() ? f.f(v.fail()) : v.success()
		} as F
	}

}

