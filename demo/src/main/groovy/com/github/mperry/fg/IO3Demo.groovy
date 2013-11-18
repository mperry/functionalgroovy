package com.github.mperry.fg

import fj.F
import fj.P1
import fj.Unit
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
class IO3Demo {

	final String quit = "q"
	final String help = "Squaring REPL\nEnter $quit to quit"
	final String prompt = ">"

	@TypeChecked
	Option<Integer> toInt(String s) {
		s.isInteger() ? Option.some(s.toInteger()) : Option.none()
	}

	@TypeChecked
	String squareMessage(Integer n) {
		"square $n = ${n * n}"
	}

	@TypeChecked
	Option<IO3<Unit>> squareIO(String s) {
		toInt(s).map { Integer n ->
			IOConstants.consoleWriteLine(squareMessage(n))
		}
	}

	@TypeChecked
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
		validMessage(s) ? Option.none() : Option.some("Not an integer: $s")
	}

	@TypeChecked
	Option<IO3<Unit>> invalidMessageIO(String s) {
		invalidMessage(s).map { String it -> IOConstants.consoleWriteLine(it)}
	}

	@TypeChecked
	IO3<?> foldIO(List<Option<IO3<?>>> list) {
		(IO3<?>) list.inject(IO3.empty()) { IO3<?> acc, Option<IO3<?>> it ->
			it.isNone() ? acc : acc.append(it.some())
		}
	}

	@TypeChecked
	Stream<IO3<String>> inputStream() {
		def s = Stream.range(1).map { Integer i ->
			def io = IOConstants.consoleWriteLine(prompt).append(IOConstants.consoleReadLine())
			io.flatMap({ String s ->
				foldIO([invalidMessageIO(s), squareIO(s)]).append(IO3.unit(s))
			} as F)
		}
	}

	@TypeChecked
	Stream<IO3<?>> stream() {
		def p = new P1<Stream<IO3<String>>>(){
			@Override
			Stream<IO3<String>> _1() {
				inputStream().takeWhile { IO3<String> io ->
					def s = io.run()
					isLoop(s)
				}
			}
		}
		Stream.cons(IOConstants.consoleWriteLine(help), p)
	}

	@TypeChecked
	void repl2() {
		stream().map { IO3 io ->
			io.run()
		}.toList()
	}

	@TypeChecked
	void repl() {
		IOConstants.consoleWriteLine(help).run()
		def s = inputStream().takeWhile { IO3<String> io ->
			def s = io.run()
			isLoop(s)
		}
		s.toList()
	}

	static void main(def args) {
		def d = new IO3Demo()
//		println "hello world"
		d.repl2()
	}

}
