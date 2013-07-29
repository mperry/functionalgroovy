package com.github.mperry.fg

import fj.F
import fj.F3
import fj.data.Option
import groovy.transform.TypeChecked
import org.junit.Test

import static junit.framework.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 29/07/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
class LiftTest {

	Properties readFile(String s) {
		def is = getClass().getClassLoader().getResourceAsStream(s);
		def p = new Properties()
		p.load(is)
		p
	}

	@Test
//	@TypeChecked
	void test1() {

		def s = "lift.properties"
		def p = readFile(s)
		def v1 = p.getProperty("lift1")
		def v2 = p.getProperty("lift2Missing")
		def v3 = getIntProperty(p, "lift3")
		def v4 = getIntProperty(p, "lift4")
		def v5 = getIntProperty(p, "lift5")
		def v6 = getIntProperty(p, "lift6")

		def opt1 = doForET(v3, v4, v5)
		def opt2 = doForEU(v3, v4, v5)
		def opt3 = doFor(v3, v4, v5)
		def opt4 = lift3(v3, v4, v5) { Integer a, Integer b, Integer c ->
			a * b * c
		}
		def opt5 = lift3C(v3, v4, v5)(this.&func)

//		def opt6 = liftF3(this.&func).f(v3, v4, v5)

//		def f = this.&func
		def f3 = {Integer a, Integer b, Integer c -> a * b * c} as F3
		def liftedF3 = Option.liftM3(f3).f(v3, v4, v5)

		def f1 = {Integer a -> 2 * a} as F
		def liftedF1 = Option.liftM1(f1)
//		def opt8 = liftedF1.f(v3)

		def o9 = Option.unit(3)
//		Option.liftM1()

//		println "$opt1 $opt2 $opt3 $opt4 $opt5 $opt6 $opt7"
	}

	Integer traditional(Integer a, Integer b, Integer c) {
		if (a == null) {
			throw new Exception("invalid arg")
		} else if (b == null) {
			throw new Exception("invalid arg")
		} else if (c == null) {
			throw new Exception("invalid arg")
		} else {
			a * b * c
		}
	}

	def <A, B, C, D> Closure<Option<D>> lift3C(Option<A> o1, Option<B> o2, Option<C> o3) {
		{ Closure c ->
			Comprehension.foreach {
				v1 << o1
				v2 << o2
				v3 << o3
				yield { c(v1, v2, v3)}
			}
		}
	}

	def <A, B, C, D> F3<Option<A>, Option<B>, Option<C>, Option<D>> liftF3(Closure c) {
		{ Option<A> o1, Option<B> o2, Option<C> o3 ->
			Comprehension.foreach {
				v1 << o1
				v2 << o2
				v3 << o3
				yield { c(v1, v2, v3)}
			}
		} as F3
	}


	static <A, B, C, D> Option<D> lift3(Option<A> o1, Option<B> o2, Option<C> o3, Closure c) {
		Comprehension.foreach {
			v1 << o1
			v2 << o2
			v3 << o3
			yield { c(v1, v2, v3)}
		}
	}


	Integer func(Integer a, Integer b, Integer c) {
		a * b * c
	}


//	@TypeChecked
	Option<Integer> doFor(Option<Integer> v1, Option<Integer> v2, Option<Integer> v3) {
		Comprehension.foreach {
			a << v1
			b << v2
			c << v3
			yield { a * b * c }
		}
	}

	@TypeChecked
	Option<Integer> doForET(Option<Integer> v1, Option<Integer> v2, Option<Integer> v3) {
		v1.bind { Integer a -> v2.bind { Integer b -> v3.map { Integer c -> a * b * c } } }
	}

	Option<Integer> doForEU(Option<Integer> v1, Option<Integer> v2, Option<Integer> v3) {
		v1.bind {
			a -> v2.bind {
				b -> v3.map {
					c -> a * b * c
				}
			}
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
