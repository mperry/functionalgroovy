package com.github.mperry.fg.euler

import fj.F
import fj.data.Enumerator
import fj.data.Stream
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.junit.Ignore
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 21/06/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */


/*
 * 2520 is the smallest number that can be divided by each of the numbers from
 * 1 to 10 without any remainder.
 *
 * What is the smallest positive number that is evenly divisible by all of the
 * numbers from 1 to 20?
 *
 */

class P05 extends GroovyTestCase {

	boolean divisible(int num, int min, int max) {
		divisible1(num, min, max)
	}

	@CompileStatic
	@TypeChecked
	boolean divisible1(int num, int min, int max) {
		def s = (1..max).takeWhile { int it -> num % it == 0 }
		def b = s.size() == max - min + 1
		b
	}

	@CompileStatic
	@TypeChecked
	boolean divisible2(int num, int min, int max) {
		min.to(max).forall({ int it -> num % it == 0 } as F)
	}

	@TypeChecked
	@CompileStatic
	int lowest(int min, int max) {
		def s3 = Stream.forever(Enumerator.intEnumerator, max, max)
//		def s = Stream.range(1)
		def s2 = s3.dropWhile({ Integer it ->
			def b = !divisible(it, min, max)
			b
		} as F)
		def val = s2.head()
		val
	}

	@Test
	void test1() {
		assertTrue(divisible(6, 1, 3))
		assertTrue(lowest(1, 3) == 6)
	}

	@Test
	void test2() {
		def val = lowest(1, 10)
		assertTrue(val == 2520)
	}

	@Test
	void testHigh() {
		def v2 = lowest(1, 20)
		assertTrue(v2 == 232792560)
	}

	@Test
	@Ignore
	void myTestMid18() {
		def val = lowest(1, 18)
		println val
		assertTrue(val == 12252240)
	}

	@Test
	@Ignore
	void myTestMid19() {
		def val = lowest(1, 19)
		println val
		assertTrue(val == 232792560)
	}


	@Test
	void test5() {
		def val = lowest(1, 6)
		println val
		assertTrue(val == 60)
	}

}
