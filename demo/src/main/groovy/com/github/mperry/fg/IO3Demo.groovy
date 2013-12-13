package com.github.mperry.fg

import fj.F
import fj.F2
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
	Option<SimpleIO<Unit>> squareIO(String s) {
		toInt(s).map { Integer n ->
			IOConstants.consoleWriteLine(squareMessage(n))
		}
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
	Boolean validMessage(String s) {
		(s.isInteger() || isQuit(s))
	}

	@TypeChecked
	Option<String> invalidMessage(String s) {
		validMessage(s) ? Option.none() : Option.some("Not an integer: $s")
	}

	@TypeChecked
	Option<SimpleIO<Unit>> invalidMessageIO(String s) {
		invalidMessage(s).map { String it -> IOConstants.consoleWriteLine(it)}
	}

	@TypeChecked
	SimpleIO<?> foldIO(List<Option<SimpleIO<?>>> list) {
		(SimpleIO<?>) list.inject(SimpleIO.empty()) { SimpleIO<?> acc, Option<SimpleIO<?>> it ->
			it.isNone() ? acc : acc.append(it.some())
		}
	}

	@TypeChecked
	Stream<SimpleIO<String>> inputStream() {
		def s = Stream.range(1).map { Integer i ->
			def io = IOConstants.consoleWriteLine(prompt).append(IOConstants.consoleReadLine())
			io.flatMap({ String s ->
				foldIO([invalidMessageIO(s), squareIO(s)]).append(SimpleIO.lift(s))
			} as F)
		}
	}

	void test1(Stream<SimpleIO<String>> stream) {
//		def i = inputStream()
		if (!stream.isEmpty()) {
			def io = stream.head()
			io.flatMap({ String s ->
				if (s != quit) {
					test1(stream.tail())
				}
			} as F)
		}
	}

	@TypeChecked
	Stream<SimpleIO<?>> stream() {
		def p = new P1<Stream<SimpleIO<String>>>(){
			@Override
			Stream<SimpleIO<String>> _1() {
				inputStream().takeWhile { SimpleIO<String> io ->
					def s = io.run()
					isLoop(s)
				}
			}
		}
		Stream.cons(IOConstants.consoleWriteLine(help), p)
	}

	@TypeChecked
	SimpleIO<?> recursiveInput(SimpleIO<?> myIo) {
		def quit = "q"
		def io = IOConstants.consoleWriteLine(prompt)
		myIo.append(io).append(IOConstants.consoleReadLine().flatMap({ String s ->
				def f = foldIO([invalidMessageIO(s), squareIO(s)]).append(SimpleIO.lift(s))

				def stop = s == quit
				if (!stop) {
					recursiveInput(f)
				} else {
					f
				}
			} as F)
		)
	}

	@TypeChecked
	void repl2() {
		def action = stream().foldLeft({ SimpleIO<?> acc, SimpleIO<?> io ->
			acc.append(io)
		} as F2<SimpleIO, SimpleIO, SimpleIO>, SimpleIO.empty())
		action.run()
//		stream().map { SimpleIO io ->
//			io.run()
//		}.toList()
	}

	SimpleIO<?> repl3() {
		IOConstants.consoleWriteLine(help).append(recursiveInput(SimpleIO.empty()))
	}

	void repl4() {
		def io = repl3()
		io.run()
	}

	@TypeChecked
	void repl() {
		IOConstants.consoleWriteLine(help).run()
		def s = inputStream().takeWhile { SimpleIO<String> io ->
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
