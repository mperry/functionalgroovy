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
	Boolean loop(String s) {
		s != quit
	}

	@TypeChecked
	Boolean isQuit(String s) {
		!loop(s)
	}

	@TypeChecked
	Boolean validInput(String s) {
		s.isInteger() || isQuit(s)
	}

	@TypeChecked
	void process(String s) {
		if (!validInput(s)) {
			println(invalidMessage(s))
		}
		if (s.isInteger()) {
			println(squareMessage(s.toInteger()))
		}
	}

	@TypeChecked
	void repl() {
		println(help)
		System.in.withReader { Reader r ->
			def isLoop = true
			while (isLoop) {
				println(prompt)
				def s = r.readLine()
				process(s)
				isLoop = loop(s)
			}
		}
	}

	static void main(def args) {
		def d = new IO3Demo()
		d.repl()
	}

}
