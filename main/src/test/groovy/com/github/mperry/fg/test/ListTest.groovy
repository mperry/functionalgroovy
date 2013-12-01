package com.github.mperry.fg.test

import groovy.transform.TypeChecked
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
	@TypeChecked
	void test1() {
		def f = { java.util.List<Integer> list1, java.util.List<Integer> list2 ->
//			(list1.append(list2)).length() == list1.length() + list2.length()
			(list1 + list2).size() == list1.size() + list2.size()
		}
		PropertyTester.showAll([(java.util.List.class): arbArrayList(arbIntegerBoundaries)], f)
//		PropertyTester.showAll([(java.util.List.class): arbList(arbIntegerBoundaries)], f)
	}

	@Test
	void test2() {
		PropertyTester.showAll([(ArrayList.class): arbArrayList(arbIntegerBoundaries)]) { ArrayList list1, ArrayList list2 ->
			(list1 + list2).size() == list1.size() + list2.size()

		}
	}

}
