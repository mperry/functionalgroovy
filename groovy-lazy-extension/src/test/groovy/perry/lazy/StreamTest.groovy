package perry.lazy

import org.junit.Test
import fj.F
import fj.data.Stream
import perry.groovy.lazy.StreamExtension

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class StreamTest {

	@Test
	void test1() {
		assertTrue(true)
	}

	@Test
	void substreams() {
		def a = Stream.stream(Stream.range(1, 3), Stream.range(2, 4))
		assertTrue(StreamExtension.toJList(a) == [[1, 2], [2, 3]])
	}

	@Test
	void mapWithSubstreams() {
		def a = Stream.range(1, 3)
		def b = a.map({Stream.range(it, 3)} as F)
		assertTrue(StreamExtension.toJList(b) == [[1, 2], [2]])
	}

	@Test
	void simple() {
		def a = Stream.range(1, 4)
		assertTrue(a.toJList() == [1, 2, 3])

	}

}
