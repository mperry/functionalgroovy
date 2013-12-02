package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.F3
import fj.F4
import fj.F5
import fj.P2
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.CheckResult
import fj.test.Property
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.codehaus.groovy.runtime.NullObject

import static fj.test.Arbitrary.*

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class PropertyTester {

	static final int MAX_ARGS = 5

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

	@TypeChecked(TypeCheckingMode.SKIP)
	static Property createProp(Closure<Boolean> c) {
		createProp(defaultMap, Option.none(), c)
	}

	static Property createProp(Map<Class<?>, Arbitrary> map, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validation) {
		def list = c.getParameterTypes()
		def arbOpts = list.collect { Class it -> map.containsKey(it) ? Option.some(map[it]) : Option.none() }
		def allMapped = arbOpts.forAll { Option it -> it.isSome() }
		if (!allMapped) {
			throw new Exception("Not all function parameter types were found: ${list.findAll { !map.containsKey(it)}}")
		}
		createProp(arbOpts.collect { Option<Arbitrary> it -> it.some() }, pre, c, validation)
	}

	static CheckResult showAllWithMap(Boolean ok, Map<Class<?>, Arbitrary> map, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validate) {
		def p = createProp(map, pre, c, validate)
		def cr = p.check()
		p.checkBooleanWithNullableSummary(ok)
	}

	/**
	 *
	 * @param map Override the default map
	 * @param c
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult showAll(Map<Class<?>, Arbitrary<?>> map, Closure<Boolean> c) {
		showAllWithMap(true, defaultMap + map, Option.none(), c, TestConfig.DEFAULT_VALIDATOR)
	}

	static CheckResult showAll(TestConfig config) {
		showAllWithMap(config.truth, config.map, config.pre, config.function, config.validator)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult showAll(Closure<Boolean> c) {
		showAllWithMap(true, defaultMap, Option.none(), c, TestConfig.DEFAULT_VALIDATOR)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static Property createProp(List<Arbitrary> list, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validate) {
		if (c.getMaximumNumberOfParameters() > MAX_ARGS) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $MAX_ARGS")
		}
		this."createProp${list.size()}"(list, pre, c, validate)
	}

	static Property implies(Boolean pre, Boolean result) {
		Bool.bool(pre).implies(result)
	}

	static Property createProp0(List<Arbitrary> list, Option<Closure<Boolean>> pre, Closure<Boolean> c) {
		def preOk = pre.map { Closure<Boolean> it -> it.call() }.orSome(true)
//		def preOk = pre.call()
		def result = !preOk ? true: c.call()
		implies(preOk, result)
	}

	@TypeChecked
	static Property createProp1(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> closure) {
		Property.property(list[0], { a ->
			def preOk = pre.map { Closure<Boolean> it -> it.call(a) }.orSome(true)
//			def preOk = pre.call(a)
			def result = !preOk ? true : closure.call(a)
			implies(preOk, result)
		} as F)
	}

//	@TypeChecked(TypeCheckingMode.SKIP)
	static Validation<Throwable, Boolean> perform(Closure<Boolean> c, List args) {
		try {
			def r = c.call(args)
			int z = 0
			def v = Validation.success(r)
			int a = 3 + 2
			return v
		} catch (Throwable t) {
			return Validation.fail(t)
		}
	}

	static void noop(Closure c) {
		int z = 0
	}

	@TypeChecked
	static Property createProp2(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], { Object a, Object b ->
			def preOk = pre.map { Closure<Boolean> it -> it.call(a, b) }.orSome(true)
			def objectTypes = [a.getClass(), b.getClass()]
			def closureTypes = func.getParameterTypes().toList()
			def typesOk = objectTypes.zip(closureTypes).inject(true) { Boolean result, P2<Class, Class> p ->
				result && ((p._1() == NullObject.class) ? true : p._2().isAssignableFrom(p._1()))
			}
			if (!typesOk || objectTypes.size() != closureTypes.size()) {
				println("Cannot call func with value types $objectTypes.  Closure requires types $closureTypes")
				return Property.prop(false)
			}

			try {
				def v = perform(func, [a, b])
				def result = !preOk ? true : validate.f(v)

//				def result = !preOk ? true : validate.f(perform(func, [a, b]))
				implies(preOk, result)
			} catch (Exception e) {
				println e.getMessage()
				Property.prop(false)
			} catch (Error e) {
				println e.getMessage()
				Property.prop(false)
			}
		} as F2)
	}

	@TypeChecked
	static Property createProp3(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], { a, b, c ->
			def preOk = pre.map { Closure<Boolean> it -> it.call(a, b, c) }.orSome(true)
			def result = !preOk ? true : closure.call(a, b, c)
			implies(preOk, result)
		} as F3)
	}

	@TypeChecked
	static Property createProp4(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], list[3], { a, b, c, d ->
			def preOk = pre.map { Closure<Boolean> it -> it.call(a, b, c, d) }.orSome(true)
			def result = !preOk ? true : closure.call(a, b, c, d)
			implies(preOk, result)
		} as F4)
	}

	@TypeChecked
	static Property createProp5(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> closure) {
		Property.property(list[0], list[1], list[2], list[3], list[4], { a, b, c, d, e ->
			def preOk = pre.map { Closure<Boolean> it -> it.call(a, b, c, d, e) }.orSome(true)
			def result = !preOk ? true : closure.call(a, b, c, d, e)
			implies(preOk, result)
		} as F5)
	}

}
