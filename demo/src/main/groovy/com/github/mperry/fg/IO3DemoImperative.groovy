package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
class IO3DemoImperative {

	String quit = "q"
	String help = "Squaring REPL\nEnter $quit to quit"
	String prompt = ">"

	@TypeChecked
	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	@TypeChecked
	String invalidMessage(String s) {
		"Not an integer: $s"
	}

	@TypeChecked
	Boolean isLoop(String s) {
		s != quit
	}

	@TypeChecked
	Boolean isQuit(String s) {
		!isLoop(s)
	}

	@TypeChecked
	Boolean validInput(String s) {
		s.isInteger() || isQuit(s)
	}

//	@TypeChecked
	void repl() {
		println(help)
		System.in.withReader {
			def isLoop = true
			while (isLoop) {
				println(prompt)
				def s = it.readLine()
				if (!validInput(s)) {
					println(invalidMessage(s))
				}
				if (s.isInteger()) {
					println(squareMessage(s.toInteger()))
				}
				isLoop = isLoop(s)
			}
		}
	}

	static void main(def args) {
		def d = new IO3Demo()
		d.repl()
	}

}
