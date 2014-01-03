package com.github.mperry.fg

import fj.F
import fj.Unit
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked

import static com.github.mperry.fg.IOConstants.stdinReadLine
import static com.github.mperry.fg.IOConstants.stdoutWriteLine
import static fj.data.Option.none
import static fj.data.Option.some

@TypeChecked
class SimpleIODemoFunctional {

	final String quit = "q"
	final String help = "The Spectacular Squaring REPL!\nEnter an integer to square or enter $quit to quit"
	final String prompt = ">"

	Option<Integer> toInt(String s) {
		s.isInteger() ? some(s.toInteger()) : none()
	}

	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	Option<SimpleIO<Unit>> squareOptionIO(String s) {
		toInt(s).map { Integer n ->
			stdoutWriteLine(squareMessage(n))
		}
	}

    SimpleIO<Unit> squareIO(String s) {
        squareOptionIO(s).orSome(IOConstants.empty())
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
		validMessage(s) ? none() : Option.<String>some("Not an integer: $s")
	}

	Option<SimpleIO<Unit>> invalidMessageOptionIO(String s) {
		invalidMessage(s).map { String it -> stdoutWriteLine(it)}
	}

    SimpleIO<Unit> invalidMessageIO(String s) {
        invalidMessageOptionIO(s).orSome(IOConstants.empty())
    }

    SimpleIO<String> interaction() {
        stdoutWriteLine(prompt).append(stdinReadLine()).flatMap1({ String s ->
            invalidMessageIO(s).append(squareIO(s))
        } as F)
    }

    SimpleIO<Stream<String>> interactionStream() {
        SimpleIO.sequenceWhile(Stream.repeat(interaction()), { String s -> isLoop(s) } as F)
    }

    SimpleIO<Stream<String>> interactionStreamAsync() {
        SimpleIO.sequenceWhile(Stream.repeat(interaction()), { String s -> isLoop(s) } as F)
    }

    SimpleIO<Stream<String>> repl() {
        stdoutWriteLine(help).append(interactionStream())
    }

	static void main(def args) {
		def d = new SimpleIODemoFunctional()
		d.repl().run()
	}

}
