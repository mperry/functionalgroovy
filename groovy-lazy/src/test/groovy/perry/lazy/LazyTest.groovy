package perry.lazy

import fj.data.Stream
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter
import org.junit.Test
import fj.F
/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 11/12/12
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
class LazyTest {

	@Test
	void test1() {
		def evens = Stream.range(0).filter{ it % 2 == 0 }
		assert evens.take(5).toJList() == [0, 2, 4, 6, 8]
	}

	@Test
	public void test2() throws Throwable {

		List l = ScriptBytecodeAdapter.createRange(new Integer(1), new Integer(5), true);
		int z = 0;

	}

	@Test
	public void test3() {

		def a = 1.to(6)
		def b = a.filter({it % 2 == 0})
		assert (b.toJList() == [2, 4, 6])

	}

}
