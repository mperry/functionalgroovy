package com.github.mperry.fg.test

import fj.*
import fj.data.Validation
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.CheckResult
import fj.test.Property
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.gcontracts.ClassInvariantViolation
import org.gcontracts.PostconditionViolation
import org.gcontracts.PreconditionViolation

import java.lang.Class
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 2/07/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked(TypeCheckingMode.SKIP)
class MethodVerifier {


	def gens = [(Integer.class): Arbitrary.arbInteger, (int.class): Arbitrary.arbInteger, (String.class): Arbitrary.arbString]
	def funcs = [1: F, 2: F2, 3: F3, 4: F4, 5: F5, 6: F6, 7: F7, 8: F8]

    @TypeChecked(TypeCheckingMode.SKIP)
	Validation<String, CheckResult> verify(Object o, String methodName) {
		def c = o.class
		def ms = c.getDeclaredMethods()
		def list2 = ms as List
		def list3 = list2.findAll {
			it.name == methodName
		}
		// TODO: does not handle mutliple methods with the same name
		verify(o, list3[0])
	}

	Validation<List<String>, CheckResult> verify(Object obj, Method m) {
		m.parameterTypes
		def arbs = createArb(m)
		def fails = arbs.findAll{it.isFail()}
		def flat = fails.flatten()
		if (flat.size() > 0) {
			Validation.fail(flat)
		} else {
			def flatArbs = arbs.collect { it.success()}
			def p = createProp(obj, m, flatArbs)
			def cr = p.check()
			CheckResult.summary.println(cr)
			Validation.success(cr)
		}
	}

	List<Validation<String, Arbitrary>> createArb(Method m) {
		m.parameterTypes.collect {
			gens.containsKey(it) ? Validation.success(gens[it]) : Validation.fail("parameter type $it not supported by ${this.class.name}")
		}
	}

	Class funcType(List list) {
		funcs[list.size()]
	}

	Property createProp(Object obj, Method m, List arbs) {
		"createProp${arbs.size()}"(obj, m, arbs)
	}

	Property createProp1(Object obj, Method m, List arbs) {
		def p = Property.property(arbs[0], { def a ->
			common(obj, m, [a])
		}.asType(funcType(arbs)))
		p
	}

	Property createProp2(Object obj, Method m, List arbs) {
		def p = Property.property(arbs[0], arbs[1], { def a, def b ->
			common(obj, m, [a, b])
		}.asType(funcType(arbs)))
		p
	}

	Property createProp3(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], { def a, def b, def c ->
			common(obj, m, [a, b, c])
		}.asType(funcType(arbs)))
	}

	Property createProp4(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], arbs[3], { def a, def b, def c, def d ->
			common(obj, m, [a, b, c, d])
		}.asType(funcType(arbs)))
	}

	Property createProp5(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], arbs[3], arbs[4], { def a, def b, def c, def d, def e ->
			common(obj, m, [a, b, c, d, e])
		}.asType(funcType(arbs)))
	}

	Property createProp6(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], arbs[3], arbs[4], arbs[5], { def a, def b, def c, def d, def e, def f ->
			common(obj, m, [a, b, c, d, e, f])
		}.asType(funcType(arbs)))
	}

	Property createProp7(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], arbs[3], arbs[4], arbs[5], arbs[6], { def a, def b, def c, def d, def e, def f, def g ->
			common(obj, m, [a, b, c, d, e, f, g])
		}.asType(funcType(arbs)))
	}

	Property createProp8(Object obj, Method m, List arbs) {
		Property.property(arbs[0], arbs[1], arbs[2], arbs[3], arbs[4], arbs[5], arbs[6], arbs[7], { def a, def b, def c, def d, def e, def f, def g, def h ->
			common(obj, m, [a, b, c, d, e, f, g, h])
		}.asType(funcType(arbs)))
	}

	Property common(Object obj, Method m, List args) {
		def ok = true
		def preOk = true
		try {
			m.invoke(obj, args.toArray())
		} catch (PreconditionViolation e) {
			preOk = false
		} catch (PostconditionViolation e) {
			ok = false
		} catch (ClassInvariantViolation e) {
			ok = false
		} catch (InvocationTargetException e) {
			def t = e.targetException
			if (t instanceof PreconditionViolation) {
				preOk = false
			} else if (t instanceof PostconditionViolation || e instanceof ClassInvariantViolation) {
				ok = false
			}
		} catch (Exception e) {
			ok = false
		} catch (Error e) {
			ok = false
		}
		Bool.bool(preOk).implies(ok)
	}

}
