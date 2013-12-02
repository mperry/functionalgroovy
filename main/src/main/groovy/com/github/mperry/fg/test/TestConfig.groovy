package com.github.mperry.fg.test

import fj.F
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import groovy.transform.Canonical
import groovy.transform.Immutable
import groovy.transform.TypeChecked

import static fj.test.Arbitrary.*

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

	static final Map NULLABLE_INTEGER = [(Integer.class): Arbitrary.arbNullableInteger()]

	final static Map<Class<?>, Arbitrary> defaultMap = [
			// basic generators
			(BigDecimal.class): arbBigDecimal,
			(BigInteger.class): arbBigInteger,
			(BitSet.class): arbBitSet,
			(Boolean.class): arbBoolean,
			(Byte.class): arbByte,
			(Calendar.class): arbCalendar,
			(Character.class): arbCharacterBoundaries,
			(Date.class): arbDate,
			(Double.class): arbDoubleBoundaries,
			(Float.class): arbFloatBoundaries,
			(Integer.class): arbIntegerBoundaries,
			(Long.class): arbLongBoundaries,
			(String.class): arbString,

			// more complex generators
			(ArrayList.class): arbArrayList(arbIntegerBoundaries),
			(java.util.List.class): arbArrayList(arbIntegerBoundaries),
			(fj.data.List.class): arbList(arbIntegerBoundaries)
	]

	static final F<Validation<Throwable, Boolean>, Boolean> DEFAULT_VALIDATOR = { Validation<Throwable, Boolean> v ->
		v.isFail() ? false : v.success()
	} as F

	Map<Class<?>, Arbitrary> map = defaultMap
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

