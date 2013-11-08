package com.github.mperry.fg

import com.github.mperry.fg.IO3
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 7/11/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
class IO3Test {

	@Test
	void test1() {

		def a = IO3.consoleReadLine()
		def b = a.run()
		println "Got from console: $b"
	}

	void myTest2() {
//		def a = IO3.consoleReadLine()
	}

}
