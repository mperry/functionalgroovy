package perry.lazy

import org.junit.Test
import fj.F
import groovy.transform.TypeChecked
import fj.data.Stream

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class LazyTest1 {

	@Test
	void test1() {
		def a = 1.to(6)
		def b = a.filter({it % 2 == 0})
		assert(b.toJList() == [2, 4, 6])

	}



	@Test
	void test2() {
		def a = 1.to(2)
		def b = a.map({it.to(2)})
		assert(b.toJList() == [[1, 2], [2]])
	}


	@Test
	void test3() {
		def a = Stream.stream(1.to(2), 2.to(3))
		assert(a.toJList() == [[1, 2], [2, 3]])
	}


	@Test
	void test4() {
		def a = 1.to(2)
		def b = a.bind({it.to(2)})
		assert(b.toJList() == [1, 2, 2])
	}

}



