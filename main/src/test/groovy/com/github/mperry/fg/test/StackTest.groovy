package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.Gen
import fj.test.Property
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/08/13
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */
class StackTest {

//	def log
//	def log = Logger.getLogger(StackTest.class)

	@Test
	void test1() {
		def p = Property.property(Arbitrary.arbitrary(Gen.listOf(Arbitrary.arbInteger.gen)),
			{ fj.data.List<Integer> list ->
				println list
//				Bool.bool(true)
				Property.prop(true)
			} as F
		)
		p.checkOkWithSummary()
	}

	@Test
	void test2() {
		def p = Property.property(Arbitrary.arbitrary(Gen.listOf(Arbitrary.arbInteger.gen)), Arbitrary.arbInteger,
				{ fj.data.List<Integer> list, Integer i ->
					println list
//				Bool.bool(true)
					Property.prop(true)
				} as F2
		)
		p.checkOkWithSummary()
	}

	@Test
	void intStack() {

		def p = Property.property(Arbitrary.arbitrary(Gen.listOf(Arbitrary.arbInteger.gen)), Arbitrary.arbInteger,
				{ fj.data.List<Integer> list, Integer i ->

			try {
				def uList = list.toList()
				def s = new Stack<Integer>(uList)
				def s2 = new Stack<Integer>(s)
				s.push(i)
				s.pop()
				def b = s.elements == s2.elements
				Property.prop(b)
			} catch (Exception e) {
				int z = 0
			} catch (Throwable t) {
				int x = 0
			}


		} as F2)
		p.checkOkWithSummary()
	}


}
