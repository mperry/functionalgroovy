package com.github.mperry.fg

import groovy.transform.TypeChecked

@TypeChecked
class SimpleIODemoImperative {

	String quit = "q"
	String help = "Squaring REPL\nEnter $quit to quit"
	String prompt = ">"

	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	String invalidMessage(String s) {
		"Not an integer: $s"
	}

	Boolean loop(String s) {
		s != quit
	}

	Boolean isQuit(String s) {
		!loop(s)
	}

	Boolean validInput(String s) {
		s.isInteger() || isQuit(s)
	}

	void process(String s) {
		if (!validInput(s)) {
			println(invalidMessage(s))
		}
		if (s.isInteger()) {
			println(squareMessage(s.toInteger()))
		}
	}

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
		def d = new SimpleIODemoImperative()
		d.repl()
	}

}
