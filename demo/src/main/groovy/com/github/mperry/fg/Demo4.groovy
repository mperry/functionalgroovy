package com.github.mperry.fg

import fj.data.Option

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/11/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
class Demo4 {


	IO3<?> recursiveInput() {
		def quit = "q"
		IOConstants.consoleReadLine().flatMap { String s ->
			def stop = s == quit
			if (!stop) {
				recursiveInput()
			} else {
				IO3.empty()
			}
		}
	}


}
