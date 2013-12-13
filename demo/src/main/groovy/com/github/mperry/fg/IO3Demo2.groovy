package com.github.mperry.fg

import fj.F
import fj.Unit
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked

import static com.github.mperry.fg.IOConstants.consoleReadLine
import static com.github.mperry.fg.IOConstants.consoleWriteLine

@TypeChecked
class IO3Demo2 {

	final String quit = "q"
	final String help = "Squaring REPL\nEnter $quit to quit"
	final String prompt = ">"

	Option<Integer> toInt(String s) {
		s.isInteger() ? Option.some(s.toInteger()) : Option.none()
	}

	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	Option<IO3<Unit>> squareOptionIO(String s) {
		toInt(s).map { Integer n ->
			consoleWriteLine(squareMessage(n))
		}
	}

    IO3<Unit> squareIO(String s) {
        squareOptionIO(s).orSome(IO3.empty())
    }

    Boolean isLoop(String s) {
		s != quit
	}

	Boolean isQuit(String s) {
		!isLoop(s)
	}

	Boolean validMessage(String s) {
		(s.isInteger() || isQuit(s))
	}

	Option<String> invalidMessage(String s) {
		validMessage(s) ? Option.none() : Option.<String>some("Not an integer: $s")
	}

	Option<IO3<Unit>> invalidMessageOptionIO(String s) {
		invalidMessage(s).map { String it -> consoleWriteLine(it)}
	}

    IO3<Unit> invalidMessageIO(String s) {
        invalidMessageOptionIO(s).orSome(IO3.empty())
    }

    IO3<String> interaction() {
        consoleWriteLine(prompt).append(consoleReadLine()).flatMap1({ String s ->
            invalidMessageIO(s).append(squareIO(s))
        } as F)
    }

    IO3<Stream<String>> interactionStream() {
        def w = interaction()
        IO3.sequenceWhile(Stream.repeat(w), { String s -> s != quit } as F)
    }

    IO3<Stream<String>> repl() {
        consoleWriteLine(help).append(interactionStream())
    }

	static void main(def args) {
		def d = new IO3Demo2()
		d.repl().run()
	}

}
