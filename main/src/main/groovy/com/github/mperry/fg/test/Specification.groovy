package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.F3
import fj.F4
import fj.F5
import fj.F6
import fj.F7
import fj.F8
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
class Specification {

	static final int MAX_ARGS = 5
	static final Map<Integer, Class<?>> FUNC_TYPES = [1: F, 2: F2, 3: F3, 4: F4, 5: F5, 6: F6, 7: F7, 8: F8]

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

	static Property showAllWithMap(Boolean truth, Map<Class<?>, Arbitrary> map, Option<Closure<Boolean>> pre, Closure<Boolean> c, F<Validation<Throwable, Boolean>, Boolean> validate) {
		if (c.getMaximumNumberOfParameters() > MAX_ARGS) {
			throw new Exception("Testing does not support ${c.getMaximumNumberOfParameters()}, maximum supported is $MAX_ARGS")
		}
		def p = createProp(map, pre, c, validate)
		p
//		def cr = p.check()
//		p.checkBooleanWithNullableSummary(truth)
	}

	/**
	 *
	 * @param map Override the default map
	 * @param c
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	static Property spec(Map<Class<?>, Arbitrary<?>> map, Closure<Boolean> c) {
		def p = showAllWithMap(true, Model.DEFAULT_MAP + map, Option.none(), c, Model.DEFAULT_VALIDATOR)
		p
//		check(p, true)

	}

	static Property spec(Model config) {
		def p = showAllWithMap(config.truth, config.map, config.pre, config.function, config.validator)
		p
//		check(p, config.truth)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static Property spec(Closure<Boolean> c) {
		def p = showAllWithMap(true, Model.DEFAULT_MAP, Option.none(), c, Model.DEFAULT_VALIDATOR)
		p
//		check(p, true)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult specAssert(Map<Class<?>, Arbitrary<?>> map, Closure<Boolean> c) {
		check(spec(map, c), true)
	}

	static CheckResult specAssert(Model config) {
		check(spec(config), config.truth)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult specAssert(Closure<Boolean> c) {
//		def p = showAllWithMap(true, Model.DEFAULT_MAP, Option.none(), c, Model.DEFAULT_VALIDATOR)
		check(spec(c), true)
	}

	static CheckResult check(Property p, Boolean truth) {
//		def cr = p.check()
		p.checkBooleanWithNullableSummary(truth)
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

	static Class funcType(List list) {
		FUNC_TYPES[list.size()]
	}

	static Property createProp0(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		callCommon([], pre, func, validate)
	}

	static Property createProp1(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], { a ->
			callCommon([a], pre, func, validate)
//		}.asType(funcType(list)))
		} as F)
	}

	static Property createProp2(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], { Object a, Object b ->
			callCommon([a, b], pre, func, validate)
		} as F2)
	}

	static Property createProp3(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], list[2], { a, b, c ->
			callCommon([a, b, c], pre, func, validate)
		} as F3)
	}

	static Property createProp4(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], list[2], list[3], { a, b, c, d ->
			callCommon([a, b, c, d], pre, func, validate)
		} as F4)
	}

	static Property createProp5(List<Arbitrary<?>> list, Option<Closure<Boolean>> pre, Closure<Boolean> func, F<Validation<Throwable, Boolean>, Boolean> validate) {
		Property.property(list[0], list[1], list[2], list[3], list[4], { a, b, c, d, e ->
			callCommon([a, b, c, d, e], pre, func, validate)
		} as F5)
	}

}
