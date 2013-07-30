package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.P
import fj.P2
import fj.data.Option
import groovy.transform.TypeChecked
import org.junit.Test

import static junit.framework.Assert.assertTrue

/**
 * Example of lifting a function into the Option monad and binding
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 29/07/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
class LiftTest {

	def filename = "lift.properties"

	Properties readFile() {
		readFile(filename)
	}

	Properties readFile(String s) {
		def is = getClass().getClassLoader().getResourceAsStream(s);
		def p = new Properties()
		p.load(is)
		p
	}

	Integer calculateStandard(Properties p) {
		def v1 = p.getProperty("var1")
		def v2 = p.getProperty("var2Missing")
		try {
			if (v1 == null || v2 == null) {
				return null
			} else {
				def i = Integer.parseInt(v1)
				def j = Integer.parseInt(v2)
				return i * j
			}
		} catch (Exception e) {
			return null
		}
	}

	Option<Integer> calculateWithComprehension(Properties p) {
		def t = P.p("var3", "var4")
		def t2 = t.map({ String s -> getIntProperty(p, s)} as F, t)
		def f2 = {Integer a, Integer b -> a * b} as F2
		Comprehension.foreach {
			a << t2._1()
			b << t2._2()
			yield { f2.f(a, b) }
		}
	}

	@Test
	void testBind() {
		println calculateWithBind(readFile())
	}

	@Test
	void testComprehension() {
		println calculateWithComprehension(readFile())
	}

	Option<Integer> calculateOptionLift(Properties p) {
		def t = P.p("var3", "var4")
		def t2 = P2.map({ String s -> getIntProperty(p, s)} as F, t)
		def f2 = {Integer a, Integer b -> a * b} as F2
		Option.liftM2(f2).f(t2._1(), t2._2())
	}

	//	@TypeChecked
	Option<Integer> calculateWithBind(Properties p) {
		def t = P.p("var3", "var4")
		def t2 = P2.map({ String s -> getIntProperty(p, s)} as F, t)
		def f2 = {Integer a, Integer b -> a * b} as F2
		t2._1().bind { Integer a ->
			t2._2().map { Integer b ->
				f2.f(a, b)
			}
		}
	}



	Option<Integer> calculateOptionLiftComprehension(Properties p) {
		def t = P.p("var3", "var4")
		def t2 = P2.map({ String s -> getIntProperty(p, s)} as F, t)
		def f2 = {Integer a, Integer b -> a * b} as F2
		liftOption2(f2).f(t2._1(), t2._2())
	}

	def <A, B, C> Option<Integer> calculateWithLifter(Properties p, F<F2<A, B, C>, F2<Option<A>, Option<B>, Option<C>>> f) {
		def t = P.p("var3", "var4")
		def t2 = P2.map({ String s -> getIntProperty(p, s)} as F, t)
		def f2 = {Integer a, Integer b -> a * b} as F2
		f.f(f2).f(t2._1(), t2._2())
	}

	def <A, B, C> F2<Option<A>, Option<B>, Option<C>> liftOption2(F2<A, B, C> f) {
		{ Option<A> oa, Option<B> ob ->
			Comprehension.foreach {
				a << oa
				b << ob
				yield {
					f.f(a, b)
				}
			}
		} as F2
	}

	@Test
	void testWithSeparateLifting() {
		def o1 = calculateOptionLift(readFile())
		def o2 = calculateOptionLiftComprehension(readFile())
		println "$o1 $o2"
	}

	@Test
	void testStandardImpl() {
		def i = calculateStandard(readFile())
		println i
	}

	/**
	 * Option.liftM2 is overloaded so turning this method into a closure is
	 * ambiguous.  Use this helper method for explicit resolution.
	 */
	def <A, B, C> F2<Option<A>, Option<B>, Option<C>> liftClosureHelper(F2<A, B, C> f) {
		Option.liftM2(f)
	}

	@Test
	void testLifting() {
		def i = 0
		[this.&liftOption2, this.&liftClosureHelper].collect { Closure it ->
			println calculateWithLifter(readFile(), it as F)
		}
	}

	@TypeChecked
	Option<String> getStringProperty(Properties p, String key) {
		Option.fromNull(p.getProperty(key))
	}

	@TypeChecked
	Option<Integer> getIntProperty(Properties p, String key) {
		getStringProperty(p, key).bind { String s ->
			!s.isInteger() ? Option.none() : Option.fromNull(s.toInteger())
		}
	}

}
