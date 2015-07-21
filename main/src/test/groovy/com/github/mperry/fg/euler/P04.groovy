package com.github.mperry.fg.euler

import com.github.mperry.fg.Comprehension
import fj.F2
import fj.P
import fj.P2
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Ignore
import org.junit.Test

/*
 * A palindromic number reads the same both ways. The largest palindrome made from
 * the product of two 2-digit numbers is 9009 = 91 * 99.
 *
 * Find the largest palindrome made from the product of two 3-digit numbers.
 *
 */
//@TypeChecked
@TypeChecked(TypeCheckingMode.SKIP)
class P04 extends GroovyTestCase {

	@TypeChecked
	P2<Integer, Integer> limits(int i) {
		P.p((i / 10).intValue(), i - 1)
	}

	@TypeChecked
	Stream<Integer> p(int lim) {
		intStream(lim).filter { Integer i -> isPalindrome(i.toString()) }
	}

	Stream<Integer> intStream(int limit) {
		Comprehension.foreach {
			def p = limits(limit)
			def s = p._1().to(p._2())
			a << s
			b << {a.to(p._2())}
			yield {
				a * b
			}
		}
	}

	@TypeChecked
	Option<Integer> palindrome(int a) {
		Option.some(a).filter { isPalindrome(it.toString()) }
	}

	@TypeChecked
	boolean isPalindrome(String s) {
		s.reverse().equals(s)
	}

	@TypeChecked
	int highest(int limit) {
		p(limit).foldLeft({ Integer acc, Integer val ->
			acc > val ? acc : val
		} as F2, 0)
	}

	@Test
	void testVeryLow() {
		highest(10)
	}

	@TypeChecked
	@Test
	void testLow() {
		assertTrue(highest(100) == 9009)
	}

	@TypeChecked
	@Test
	void testHigh() {
		def v = highest(1000)
		println v
		assertTrue(v == 906609)
	}

}
