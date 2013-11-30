package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.F3
import fj.F4
import fj.F5
import fj.data.Option
import fj.test.Arbitrary
import fj.test.Bool
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

	static Property createProp(Map<Class<?>, Arbitrary> map, Closure pre, Closure c) {
		def list = c.getParameterTypes()
		def arbOpts = list.collect { Class it -> map.containsKey(it) ? Option.some(map[it]) : Option.none() }
		def allMapped = arbOpts.forAll { it.isSome() }
		if (!allMapped) {
			throw new Exception("Not all types of closure parameters were mapped")
		}
		createProp(arbOpts.collect { it.some() }, pre, c)
	}

	static CheckResult showAllWithMap(Map<Class<?>, Arbitrary> map, Closure c) {
		def cr = createProp(map, c).check()
		CheckResult.summary.println(cr)
		Assert.assertTrue(cr.isPassed() || cr.isProven())
		cr
	}

	static CheckResult showAllWithMap(Map<Class<?>, Arbitrary> map, Closure pre, Closure c) {
		def cr = createProp(map, pre, c).check()
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
		showAllWithMap(defaultMap + map, c)
	}

	static CheckResult showAll(Map<Class<?>, Arbitrary> map, Closure pre, Closure c) {
		showAllWithMap(defaultMap + map, pre, c)
	}

	static CheckResult showAll(Closure c) {
		showAllWithMap(defaultMap, c)
	}

	static CheckResult showAll(Closure pre, Closure c) {
		showAllWithMap(defaultMap, pre, c)
	}


	static Property createProp(List<Arbitrary> list, Closure<Boolean> c) {
		if (c.getMaximumNumberOfParameters() > maxArgs) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $maxArgs")
		}
		this."createProp${list.size()}"(list, c)
	}

	static Property createProp(List<Arbitrary> list, Closure<Boolean> pre, Closure<Boolean> c) {
		if (c.getMaximumNumberOfParameters() > maxArgs) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $maxArgs")
		}
		this."createProp${list.size()}"(list, pre, c)
	}


	static Property implies(Boolean pre, Boolean result) {
		Bool.bool(pre).implies(result)
	}

	static Property createProp0(List<Arbitrary> list, Closure<Boolean> c) {
		createProp0(list, { -> true }, c)
//		Property.prop(c.call())
	}

	static Property createProp0(List<Arbitrary> list, Closure<Boolean> pre, Closure<Boolean> c) {
		def preOk = pre.call()
		def result = !preOk ? true: c.call()
		implies(preOk, result)
//		Property.prop(c.call())
	}

	@TypeChecked
	static Property createProp1(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		createProp1(list, { a -> true }, closure)
//		Property.property(list[0], { a ->
//			Property.prop(closure.call(a))
//		} as F)
	}

	@TypeChecked
	static Property createProp1(List<Arbitrary<?>> list, Closure<Boolean> pre, Closure<Boolean> closure) {
		Property.property(list[0], { a ->
			def preOk = pre.call(a)
			def result = !preOk ? true : closure.call(a)
			implies(preOk, result)
//			Property.prop(closure.call(a))
		} as F)
	}

	@TypeChecked
	static Property createProp2(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		createProp2(list, { a, b -> true }, closure)
//		Property.property(list[0], list[1], { a, b ->
//			Property.prop(closure.call(a, b))
//		} as F2)
	}

	@TypeChecked
	static Property createProp2(List<Arbitrary<?>> list, Closure<Boolean> pre, Closure<Boolean> closure) {
		Property.property(list[0], list[1], { a, b ->
			def preOk = pre.call(a, b)
			def result = !preOk ? true : closure.call(a, b)
			implies(preOk, result)
//			Bool.bool(preOk).implies(ok)
//			Property.prop(closure.call(a, b))
		} as F2)
	}

	@TypeChecked
	static Property createProp3(List<Arbitrary<?>> list, Closure<Boolean> closure) {
		createProp3(list, { a, b, c -> true }, closure)
//		Property.property(list[0], list[1], list[2], { a, b, c ->
//			Property.prop(closure.call(a, b, c))
//		} as F3)
	}


	@TypeChecked
	static Property createProp3(List<Arbitrary<?>> list, Closure<Boolean> pre, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], { a, b, c ->
			def preOk = pre.call(a, b, c)
			def result = !preOk ? true : closure.call(a, b, c)
			implies(preOk, result)
//			Property.prop(closure.call(a, b, c))
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
