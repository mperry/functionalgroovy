package perry.lazy

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 18/12/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
class LazyRangeElementTest extends GroovyTestCase {

//	@LazyTest
	void test1() {
		assert(true)
	}

	void test2() {
		def a = new LazyRangeInteger(1)
		def b = new LazyRangeInteger(5)
		def c = (a..b)
		c.each {
			println it
		}

	}

}
