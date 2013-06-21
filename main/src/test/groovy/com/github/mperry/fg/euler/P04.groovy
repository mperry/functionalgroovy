package com.github.mperry.fg.euler

import com.github.mperry.fg.Comprehension
import fj.P
import fj.P2
import fj.data.Option
import fj.data.Stream
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 20/06/13
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */

/*
 * A palindromic number reads the same both ways. The largest palindrome made from
 * the product of two 2-digit numbers is 9009 = 91 * 99.
 *
 * Find the largest palindrome made from the product of two 3-digit numbers.
 *
 */

class P04 extends GroovyTestCase {


	final int limit = 1000
	int min = limit / 10
	int max = limit - 1
	Stream<Integer> p() {
//		def list = min.to(max).combos(min.to(max))
		def list = Comprehension.foreach {
			def mymax = limit
			def s = (limit - max).to(limit - min)
			a << (s)
			b << (s)
			yield {
				P.p(mymax - a, mymax - b)
			}
		}
		def a = list.map { P2 p ->
			pal(p._1(), p._2())
		}.filter { it.isSome() }
//		println list
		a
	}

	Option<Integer> pal(int a, int b) {
		Option.some(a * b).filter { isPalindrome(it.toString()) }
	}

	boolean isPalindrome(String s) {
		s.reverse().equals(s)
	}

	@Test
	void test() {
		def a = p()
		def l = a.first()
		println l
		def b = a.toJList()
		println b
		def d = b.map { it.orSome(0) }

		def c = d.sort()
		println c
		def e = c.last()
		println e

		assertTrue(e == 906609)
	}
}
