package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.F3
import fj.F4
import fj.F5
import fj.data.Option
import fj.test.Arbitrary
import fj.test.CheckResult
import fj.test.Property
import groovy.transform.TypeChecked
import org.junit.Assert

import static fj.test.Arbitrary.*

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
class PropertyTester {

	static int maxArgs = 5


	static Map<Class<?>, Arbitrary> defaultMap = [
			(BigDecimal.class): arbBigDecimal,
			(BigInteger.class): arbBigInteger,
			(BitSet.class): arbBitSet,
			(Boolean.class): arbBoolean,
			(Byte.class): arbByte,
			(Calendar.class): arbCalendar,
			(Character.class): arbCharacter,
			(Date.class): arbDate,
			(Double.class): arbDouble,
			(Float.class): arbFloat,
			(Integer.class): arbInteger,
			(Long.class): arbLong,
			(String.class): arbString
	]

	static Property createProp(Closure c) {
		createProp(defaultMap, c)
	}

	static Property createProp(Map<Class<?>, Arbitrary> map, Closure c) {
		def list = c.getParameterTypes()
		def arbOpts = list.collect { Class it -> map.containsKey(it) ? Option.some(map[it]) : Option.none() }
		def allMapped = arbOpts.forAll { it.isSome() }
		if (!allMapped) {
			throw new Exception("Not all types of closure parameters were mapped")
		}
		createProp(arbOpts.collect { it.some() }, c)
	}

	static CheckResult showAllActual(Map<Class<?>, Arbitrary> map, Closure c) {
		def cr = createProp(map, c).check()
		CheckResult.summary.println(cr)
		Assert.assertTrue(cr.isPassed() || cr.isProven())
		cr
	}

	/**
	 *
	 * @param map Override the default map
	 * @param c
	 */
	static CheckResult showAll(Map<Class<?>, Arbitrary> map, Closure c) {
		showAllActual(defaultMap + map, c)
	}

	static CheckResult showAll(Closure c) {
		showAllActual(defaultMap, c)
	}


	static Property createProp(List<Arbitrary> list, Closure<Boolean> c) {
		if (c.getMaximumNumberOfParameters() > maxArgs) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $maxArgs")
		}
		this."createProp${list.size()}"(list, c)
	}

	static Property createProp0(List<Arbitrary> list, Closure<Boolean> c) {
		Property.prop(c.call())
	}

	@TypeChecked
	static Property createProp1(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		Property.property(list[0], { a ->
			Property.prop(closure.call(a))
		} as F)
	}

	@TypeChecked
	static Property createProp2(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		Property.property(list[0], list[1], { a, b ->
			Property.prop(closure.call(a, b))
		} as F2)
	}

	@TypeChecked
	static Property createProp3(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], { a, b, c ->
			Property.prop(closure.call(a, b, c))
		} as F3)
	}

	@TypeChecked
	static Property createProp4(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], list[3], { a, b, c, d ->
			Property.prop(closure.call(a, b, c, d))
		} as F4)
	}

	@TypeChecked
	static Property createProp5(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], list[3], list[4], { a, b, c, d, e ->
			Property.prop(closure.call(a, b, c, d, e))
		} as F5)
	}

}
