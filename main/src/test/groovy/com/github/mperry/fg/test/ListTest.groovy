package com.github.mperry.fg.test

import org.junit.Test

import static fj.test.Arbitrary.*
import fj.data.List

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
		PropertyTester.showAll([(List.class): arbList(arbIntegerBoundaries)]) { List<Integer> list1, List<Integer> list2 ->
			(list1.append(list2)).length() == list1.length() + list2.length()
		}
	}
}
