package com.github.mperry.fg

import org.junit.Test

import static junit.framework.Assert.assertTrue
import static com.github.mperry.fg.Comprehension.foreach

/**
 * @deprecated
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
// Test examples from http://jira.codehaus.org/browse/GROOVY-4105
class ComprehensionTest {

	@Test
	void simple() {
		def expected = [4, 5, 5, 6]
		def res = foreach {
			a = 1.to(2)
			b = 3.to(4)
			yield {
				a + b
			}
		}
		assertTrue(res == expected)
	}

//	[ (x,y) for x in range(5) for y in range(3) if (y+x) % (x+2) == 0 ]  // python
	// guards not implemented
	@Test
	void testPythonExample() {
		def list = foreach {
			x = 1.to(2)
			y = 1.to(3)
			yield {
				[x, y]
			}
		}
		def expected = [[1, 1], [1, 2], [1, 3], [2, 1], [2, 2], [2, 3]]
		assertTrue(list == expected)
		assertTrue(expected == 1.to(2).combos(1.to(3)).toJList())
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
