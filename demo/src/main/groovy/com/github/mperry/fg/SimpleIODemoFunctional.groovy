package com.github.mperry.fg

import fj.F
import fj.Unit
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked

import static com.github.mperry.fg.IOConstants.consoleReadLine
import static com.github.mperry.fg.IOConstants.consoleWriteLine

@TypeChecked
class SimpleIODemoFunctional {

	final String quit = "q"
	final String help = "Squaring REPL\nEnter $quit to quit"
	final String prompt = ">"

	Option<Integer> toInt(String s) {
		s.isInteger() ? Option.some(s.toInteger()) : Option.none()
	}

	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	Option<SimpleIO<Unit>> squareOptionIO(String s) {
		toInt(s).map { Integer n ->
			consoleWriteLine(squareMessage(n))
		}
	}

    SimpleIO<Unit> squareIO(String s) {
        squareOptionIO(s).orSome(SimpleIO.empty())
    }

    Boolean isLoop(String s) {
        !isQuit(s)
	}

	Boolean isQuit(String s) {
        s == quit
	}

	Boolean validMessage(String s) {
		(s.isInteger() || isQuit(s))
	}

	Option<String> invalidMessage(String s) {
		validMessage(s) ? Option.none() : Option.<String>some("Not an integer: $s")
	}

	Option<SimpleIO<Unit>> invalidMessageOptionIO(String s) {
		invalidMessage(s).map { String it -> consoleWriteLine(it)}
	}

    SimpleIO<Unit> invalidMessageIO(String s) {
        invalidMessageOptionIO(s).orSome(SimpleIO.empty())
    }

    SimpleIO<String> interaction() {
        consoleWriteLine(prompt).append(consoleReadLine()).flatMap1({ String s ->
            invalidMessageIO(s).append(squareIO(s))
        } as F)
    }

    SimpleIO<Stream<String>> interactionStream() {
        SimpleIO.sequenceWhile(Stream.repeat(interaction()), { String s -> isLoop(s) } as F)
    }

    SimpleIO<Stream<String>> repl() {
        consoleWriteLine(help).append(interactionStream())
    }

	static void main(def args) {
		def d = new SimpleIODemoFunctional()
		d.repl().run()
	}

}
