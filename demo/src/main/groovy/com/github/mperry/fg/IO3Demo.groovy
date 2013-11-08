package com.github.mperry.fg

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
class IO3Demo {

	static void main(def args) {

		def w = IOConstants.consoleWriteLine("Get input:")
		w.run()
		def a = IOConstants.consoleReadLine()
		def b = a.run()
		println "Got from console: $b"
	}

}
