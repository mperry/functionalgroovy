package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.Show
import fj.data.Stream
import fj.test.*
import groovy.transform.Canonical
import org.gcontracts.ClassInvariantViolation
import org.gcontracts.PostconditionViolation
import org.gcontracts.PreconditionViolation
import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Requires
import org.junit.Test

@Canonical
class DbcTest extends GroovyTestCase {

	Integer sum(Integer a, Integer b) {
		a + b
	}

	@Test
	void test1() {
		def p = DbcProperties.test1Prop()
		def cr = p.check()
		CheckResult.summary.println(cr)
		assertTrue(cr.isPassed() || cr.isProven())
	}


	@Test
	void test2() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			Bool.bool(a > 0).implies(true)
		} as F2)
		def cr = p.check()
		CheckResult.summary.println(cr)
		assertTrue(cr.isPassed())
	}

	@Test
	void test3() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			def preOk = (a >= 0 && b >= 0)
			// executing sum2 seems to make it lose discarded results, so do this lazily
			def t = !preOk ? true : (sum2(a, b) == sum2(b, a))
			Bool.bool(preOk).implies(t)
		} as F2)
//		def cr = p.check(5, 500, 0, 100)
		def cr = p.check()
		CheckResult.summary.println(cr)
		assertTrue(cr.isPassed() || cr.isProven())
	}

	@Requires({a >= 0 && b >= 0})
	@Ensures({ result >= 0})
	Integer sum2(Integer a, Integer b) {
		a + b
	}

	@Test
	void test6() {
		def v = new MethodVerifier().verify(this, "sum2")
		if (v.isFail()) {
			println v.fail()
		}
		v.map({ assertTrue(it.isPassed() || it.isProven())} as F)
	}

	@Test
	void test7() {
		ExpandoMetaClass.enableGlobally()
		Show.metaClass.static.anyShow =  { -> DbcTest.anyShow() }
		def arb = Arbitrary.arbitrary(Gen.oneOf(fj.data.List.list([Gen.value(null), Arbitrary.arbInteger.gen].toArray())))
		def p = Property.property(arb, Arbitrary.arbInteger, { Integer a, Integer b ->
//			println "$a $b"
			try {
				Property.prop(sum(a, b) == sum(b, a))
			} catch (Exception e) {
				Property.prop(false)
			}
		} as F2)
		def cr = p.check()
		summary().println(cr)
//		CheckResult.summary(Arg.argShow).println(cr)
//		CheckResult.summary.println(cr)
		assertFalse(cr.isPassed() || cr.isProven())
	}

	Show<CheckResult> summary() {
		CheckResult.summary(argShow())
	}

	Show<Arg<?>> argShow() {
		Show.showS(new F<Arg<?>, String>() {
			public String f(final Arg<?> arg) {
				return Show.anyShow().showS(arg.value) +
						(arg.shrinks > 0 ? " (" + arg.shrinks + " shrink" + (arg.shrinks == 1 ? "" : 's') + ')' : "");
			}
		});
	}

	public static <A> Show<A> anyShow() {
		def c = { def a ->
			println "DbcTest.anyShow"
			//			Stream.fromString(a.toString());
			Stream.fromString(a == null ? "null" : a.toString())
		}
		new Show<A>(c as F<A, Stream<Character>>);
	}

	void test8() {
		ExpandoMetaClass.enableGlobally()
		Show.metaClass.static.anyShow =  { -> DbcTest.anyShow() }
//		ExpandoMetaClass.enableGlobally()
		def sh1 = Show.anyShow()
		println sh1
		sh1.println(1)
	}

}
