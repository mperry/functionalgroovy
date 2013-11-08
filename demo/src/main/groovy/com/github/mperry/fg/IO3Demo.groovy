package com.github.mperry.fg

import fj.F
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

	String quit = "q"
	String help = "Squaring REPL\nEnter $quit to quit"

	@TypeChecked
	Option<Integer> toInt(String s) {
		s.isInteger() ? Option.some(s.toInteger()) : Option.none()
	}



	@TypeChecked
	Option<IO3<Unit>> squareIO(String s) {
		toInt(s).map { Integer n ->
			IOConstants.consoleWriteLine("squareIO $n = ${n * n}")
		}
	}

	@TypeChecked
	Boolean isLoop(String s) {
		s != quit
	}

	Boolean isQuit(String s) {
		!isLoop(s)
	}

	@TypeChecked
	Option<IO3<Unit>> invalidMessage(String s) {
		(s.isInteger() || isQuit(s)) ? Option.none() : Option.some(IOConstants.consoleWriteLine("Not an integer: $s"))
	}

	@TypeChecked
	IO3<?> foldIO(List<Option<IO3<?>>> list) {
		(IO3<?>) list.filter { Option<?> it ->
			it.isSome()
		}.inject(IO3.empty()) { IO3<?> acc, Option<IO3<?>> it ->
			acc.append(it.some())
		}
	}

	@TypeChecked
	Stream<IO3<String>> stream() {
		def s = Stream.range(1).map { Integer i ->
			IOConstants.consoleWriteLine(">").append(IOConstants.consoleReadLine())
		}.map { IO3<String> io1 ->
			io1.flatMap({ String s ->
				foldIO([invalidMessage(s), squareIO(s)]).append(IO3.unit(s))
			} as F)
		}
	}

	@TypeChecked
	void repl() {


		IOConstants.consoleWriteLine(help).run()
		def s = stream().takeWhile { IO3<String> io2 ->
			def s = io2.run()
			isLoop(s)
		}
		s.toList()
	}

	static void main(def args) {
		def d = new IO3Demo()
		d.repl()
	}

}
