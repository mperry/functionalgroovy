package perry.lazy

import org.junit.Test
import fj.F
import fj.data.Stream
import perry.groovy.lazy.StreamExtension

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class Test1 {

	@Test
	void test2() {
		def a = Stream.stream(Stream.range(1, 3), Stream.range(2, 4))
		assert(StreamExtension.toJList(a) == [[1, 2], [2, 3]])
	}



	@Test
	void test3() {
		def a = Stream.range(1, 3)
		def b = a.map({Stream.range(it, 3)} as F)
		assert(StreamExtension.toJList(b) == [[1, 2], [2]])
	}


}
