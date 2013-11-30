package com.github.mperry.fg.test

import org.junit.Test

import static fj.test.Arbitrary.*
//import fj.data.List

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
class ListTest {

	@Test
	void test1() {
		PropertyTester.showAll([(fj.data.List.class): arbList(arbIntegerBoundaries)]) { fj.data.List<Integer> list1, fj.data.List<Integer> list2 ->
			(list1.append(list2)).length() == list1.length() + list2.length()
		}
	}

	@Test
	void test2() {
		PropertyTester.showAll([(ArrayList.class): arbArrayList(arbIntegerBoundaries)]) { ArrayList list1, ArrayList list2 ->
			(list1 + list2).size() == list1.size() + list2.size()

		}
	}

}
