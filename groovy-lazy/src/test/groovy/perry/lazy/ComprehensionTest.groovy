package perry.lazy

import org.junit.Test

import static junit.framework.Assert.assertTrue
import static perry.lazy.Comprehension.foreach

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
class ComprehensionTest {

	@Test
	void test2() {
		def expected = [4, 5, 5, 6]
		def res = Comprehension.foreach {
			a = 1.to(2)
			b = 3.to(4)
			yield {
				a + b
			}
		}
		assertTrue(res == expected)
	}

	@Test
	void testBinding() {
		def m = [a: 1, b: 2, c: 3]
		def f = {
			a + b
		}
		f.setDelegate(m)
		def g = {
			f.setDelegate(m)
			f()
		}
		def t = g()
		assertTrue(t == 3)
	}

}
