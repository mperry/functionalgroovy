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
	void lazyComprehension() {

		def res = foreach {
			a { 1.to(2) }
			b { 3.to(4) }
			yield {
				[a, b]
			}
		}
//		println res
//		println(res.toJList())
		def expected = [[1, 3], [1, 4], [2, 3], [2, 4]]
		assertTrue(expected == res.toJList())
	}

}
