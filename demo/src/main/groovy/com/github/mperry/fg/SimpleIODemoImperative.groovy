package com.github.mperry.fg

import groovy.transform.TypeChecked

@TypeChecked
class SimpleIODemoImperative {

	String quit = "q"
	String help = "Squaring REPL\nEnter $quit to quit"
	String prompt = ">"

    static void main(def args) {
        def d = new SimpleIODemoImperative()
        d.repl()
    }

    void repl() {
        println(help)
        System.in.withReader { java.io.Reader r ->
            def doLoop = true
            while (doLoop) {
                println(prompt)
                def s = r.readLine()
                process(s)
                doLoop = continueLoop(s)
            }
        }
    }

    String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	String invalidMessage(String s) {
		"Not an integer: $s"
	}

	Boolean continueLoop(String s) {
		s != quit
	}

	Boolean isQuit(String s) {
		!continueLoop(s)
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

}
