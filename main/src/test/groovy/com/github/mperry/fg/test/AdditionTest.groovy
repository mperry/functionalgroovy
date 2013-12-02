package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.P1
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.NULLABLE_INTEGER
import static com.github.mperry.fg.test.PropertyTester.defaultMap
import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.data.Option.some

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
class AdditionTest {

	@Test
	void commutes() {
		showAll { Integer a, Integer b ->
			a + b == b + a
		}
	}

	@Test
	void naturalsCommute() {
		showAll { Integer a, Integer b ->
			(a >= 0 && b >= 0).implies(a + b == b + a)
		}
	}

	/**
	 * Sometimes the number of tests discarded does not get printed.  This test shows how
	 * to work around that
	 */
	@Test
	void naturalsCommute2() {
		showAll new TestConfig(
			pre: Option.some { a, b -> a >= 0 && b >= 0 },
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

	@Test
	void impliesHandlingNulls1() {
		showAll(new TestConfig(
			map: defaultMap + NULLABLE_INTEGER,
			pre: some({ a, b -> a != null && b != null }),
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		))
	}

	@Test
	void impliesHandlingNulls2() {
		showAll new TestConfig(
				truth: false,
				map: [(Integer.class): Arbitrary.arbNullableInteger()],
				function: { Integer a, Integer b ->
					a + b == b + a
				}

		)
	}

	@Test
	void impliesHandlingNulls3() {
		showAll new TestConfig(
				map: [(Integer.class): Arbitrary.arbNullableInteger()],
				function: { Integer a, Integer b ->
					call2( { a + b == b + a } as P1, { Throwable t -> t.getClass() == NullPointerException.class } as F)
//					make2([NullPointerException.class], { a + b == b + a } as P1)._1()

				}
		)
	}

//	@Test
	void impliesHandlingNulls4() {
		showAll new TestConfig(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: make2([NullPointerException.class], { Integer a, Integer b ->
				a + b == b + a
			} as F2<Integer, Integer, Boolean>).toClosure()
		)
	}


	@Test
	void impliesHandlingNulls5() {
		def c = new TestConfig(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			},
			validator: { Validation<Throwable, Boolean> v ->
				v.isFail() ? v.fail().getClass() == NullPointerException.class : v.success()
			} as F
		)
		showAll c
	}


	def <A, B> F2<A, B, Boolean> make2(List<Class<?>> exceptions, F2<A, B, Boolean> c) {
		{ A a, B b ->
			try {
				c.f(a, b)
			} catch (Exception e) {
				exceptions.findFirst { it == e.getClass() }.isSome()
			} catch (Error e) {
				exceptions.findFirst { it == e.getClass() }.isSome()
			}
		} as F2<A, B, Boolean>
	}

	@TypeChecked
	Boolean call2(P1<Boolean> p, F<Throwable, Boolean> f) {
		def v = call2(p)._1()
		v.isFail() ? f.f(v.fail()) : v.success()
	}

	@TypeChecked
	def P1<Validation<Throwable, Boolean>> call2(P1<Boolean> p) {
		new P1<Validation<Throwable, Boolean>>() {
			@Override
			Validation<Throwable, Boolean> _1() {
				try {
					def b = p._1()
					return Validation.success(b)
				} catch (Throwable e) {
					return Validation.fail(e)
				}
			}
		}
	}

	def <A, B> P1<Boolean> make2(List<Class<?>> exceptions, P1<Boolean> c) {
		{ ->
			def v = call2(c)._1()
			v.isFail() ? exceptions.findFirst { v.fail() == it.getClass() }.isSome() : v.success()
		} as P1
	}

	@Test
	void test5() {
		def f = { Integer a -> a }
		def f2 = { Integer a -> a } as F
		def f3 = f as F
		def f4 = new F<Integer, Integer>(){
			@Override
			Integer f(Integer a) {
				a
			}
		}
		def f5 = f4.toClosure()


		def a1 = [f, f5].collect { it.getParameterTypes() }
		def a2 = [f2, f3, f4].collect {it.f(3)}

		int z = 0
	}

}
