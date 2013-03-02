package perry.lazy

import org.junit.Test

import static perry.lazy.LazyComprehension.foreach
import static junit.framework.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 26/02/13
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
class LazyComprehensionTest {


	@Test
	void simple() {
		def res = foreach {
			a { 1.to(2) }
			yield {
				a + 1
			}
		}
		def expected = [2, 3]
		def actual = res.toJList()
		assertTrue(expected == actual)
	}

	@Test
	void test1() {
		def res = foreach {
			a { 1.to(2) }
			b { 1.to(1) }
			yield {
				[a, b]
			}
		}
//		def expected = [[1, 3], [1, 4], [2, 3], [2, 4]]
		def expected = [[1, 1], [2, 1]]
		assertTrue(expected == res.toJList())
	}


	@Test
	void test2() {
		def res = foreach {
			a { 1.to(2) }
			b { a.to(2) }
			yield {
				[a, b]
			}
		}
		def expected = [[1, 1], [1, 2], [2, 2]]
		def actual = res.toJList()
		assertTrue(expected == actual)
	}


	@Test
	void test3() {
		def res = foreach {
			a { 1.to(2) }
			guard {
				a == 2
			}
			yield {
				a
			}
		}
		def expected = [2]
		assertTrue(expected == res.toJList())
	}
}
