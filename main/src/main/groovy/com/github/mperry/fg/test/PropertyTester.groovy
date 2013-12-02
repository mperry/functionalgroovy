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

	static Property createProp(Map<Class<?>, Arbitrary> map, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validation) {
		def list = c.getParameterTypes()
		def arbOpts = list.collect { Class it -> map.containsKey(it) ? Option.some(map[it]) : Option.none() }
		def allMapped = arbOpts.forAll { Option it -> it.isSome() }
		if (!allMapped) {
			throw new Exception("Not all function parameter types were found: ${list.findAll { !map.containsKey(it)}}")
		}
		dynamicCreateProp(arbOpts.collect { Option<Arbitrary> it -> it.some() }, pre, c, validation)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static Property dynamicCreateProp(List<Arbitrary> list, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validate) {
		this."createProp${list.size()}"(list, pre, c, validate)
	}

	static CheckResult showAllWithMap(Boolean truth, Map<Class<?>, Arbitrary> map, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validate) {
		if (c.getMaximumNumberOfParameters() > MAX_ARGS) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $MAX_ARGS")
		}
		def p = createProp(map, pre, c, validate)
		def cr = p.check()
		p.checkBooleanWithNullableSummary(truth)
	}

	/**
	 *
	 * @param map Override the default map
	 * @param c
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult showAll(Map<Class<?>, Arbitrary<?>> map, Closure<Boolean> c) {
		showAllWithMap(true, PropertyConfig.defaultMap + map, Option.none(), c, PropertyConfig.DEFAULT_VALIDATOR)
	}

	static CheckResult showAll(PropertyConfig config) {
		showAllWithMap(config.truth, config.map, config.pre, config.function, config.validator)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult showAll(Closure<Boolean> c) {
		showAllWithMap(true, PropertyConfig.defaultMap, Option.none(), c, PropertyConfig.DEFAULT_VALIDATOR)
	}

	static Property implies(Boolean pre, Boolean result) {
		Bool.bool(pre).implies(result)
	}


	static Validation<Throwable, Boolean> perform(Closure<Boolean> c, List args) {
		try {
			Validation.success(c.call(args))
		} catch (Throwable t) {
			Validation.fail(t)
		}
	}

	static void checkTypes(List<Object> argList, Closure<Boolean> func) {
		def objectTypes = argList.collect { it.getClass() }
		def closureTypes = func.getParameterTypes().toList()
		def typesOk = objectTypes.zip(closureTypes).inject(true) { Boolean result, P2<Class, Class> p ->
			result && ((p._1() == NullObject.class) ? true : p._2().isAssignableFrom(p._1()))
		}
		if (!typesOk || objectTypes.size() != closureTypes.size()) {
			throw new Exception("Cannot call func with value types $objectTypes.  Closure requires types $closureTypes")
		}
	}

	static Property callCommon(List<Object> argList, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		checkTypes(argList, func)
		def preOk = pre.map { Closure<Boolean> it -> it.call(argList) }.orSome(true)
		def result = !preOk ? true : validate.f(perform(func, argList))
		implies(preOk, result)
	}

	static Property createProp0(List<Arbitrary> list, Option<Closure<Boolean>> pre, Closure<Boolean> c) {
		def preOk = pre.map { Closure<Boolean> it -> it.call() }.orSome(true)
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

	@TypeChecked
	static Property createProp2(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], { Object a, Object b ->
			def argList = [a, b]
			callCommon(argList, pre, func, validate)
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
