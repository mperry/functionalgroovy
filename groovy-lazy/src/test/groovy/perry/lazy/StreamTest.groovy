package perry.lazy

import org.junit.Test
import fj.data.Stream

import static org.junit.Assert.assertTrue
import fj.F
//import perry.groovy.lazy.StreamExtension

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 20/02/13
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
class StreamTest {

	@Test
	void combos2() {
		def a = 1.to(2).combos(3.to(4))
		def expected = [[1, 3], [1, 4], [2, 3], [2, 4]]
		assertTrue(a.toJList() == expected)
	}

	@Test
	void combos3() {
		def b = 1.to(2).combos(3.to(4)).combos(5.to(6))
		def expected = [[1, 3, 5], [1, 3, 6], [1, 4, 5], [1, 4, 6],[2, 3, 5], [ 2, 3, 6], [2, 4, 5], [2, 4, 6]]
		assertTrue(b.toJList() == expected)
	}

	@Test
	void substreamsToJList() {
		def a = Stream.stream(1.to(2), 2.to(3))
		assertTrue(a.toJList() == [[1, 2], [2, 3]])
	}

	@Test
	void mapWithSubstreams() {
		def b = 1.to(2).map({it.to(2)})
		assertTrue(b.toJList() == [[1, 2], [2]])
	}

	@Test
	void mixedToJList() {
		def a = Stream.stream(5, 3.to(4))
		assertTrue(a.toJList() == [5, [3, 4]])
	}

	@Test
	void simpleToJList() {
		def a = 1.to(3)
		assertTrue(a.toJList() == [1, 2, 3])
	}

}
