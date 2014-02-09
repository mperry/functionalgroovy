package com.github.mperry.fg.test

import groovy.transform.TypeChecked
import org.junit.Test

import static Specification.spec
import static com.github.mperry.fg.test.Specification.specAssert

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
	void testConcatenationSize() {
		specAssert { List<Integer> list1, List<Integer> list2 ->
			(list1 + list2).size() == list1.size() + list2.size()
		}
	}

	/**
	 * Test ArrayList concatenation, uses same arbitrary generator as testConcatenationSize,
	 * but with concrete subclass
	 */
	@Test
	void testArrayListConcatenationSize() {
		specAssert { ArrayList list1, ArrayList list2 ->
			(list1 + list2).size() == list1.size() + list2.size()
		}
	}

}
