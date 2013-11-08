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

	void test1() {
		def w = IOConstants.consoleWriteLine("Get input:")
		w.run()
		def a = IOConstants.consoleReadLine()
		def b = a.run()
		println "Got from console: $b"
	}


	Option<IO3<Unit>> square(String s) {
		def result = Option.none()
		if (s.isInteger()) {
			def n = s.toInteger()
			def val = n * n
			result = Option.some(IOConstants.consoleWriteLine("square $n = $val"))
		}
		result
	}

	Boolean isQuit(String s) {
		s == quit
	}

	Option<IO3<Unit>> notNumber(String s) {
		(s.isInteger() || isQuit(s)) ? Option.none() : Option.some(IOConstants.consoleWriteLine("Not a number: $s"))
	}

	F<String, IO3<Boolean>> convert() {
		{ String s ->
//			println "got $s"
			def opt = square(s)
			def iod = Option.some(IO3.unit(isQuit(s)))
			def notNum = notNumber(s)
			[notNum, opt, iod].filter { it.isSome() }.inject(IO3.empty()) { acc, it ->
				acc.append(it.some())
			}
		} as F
	}

	Stream<IO3<Boolean>> stream() {
		def s = Stream.range(1).map { Integer i ->
			IOConstants.consoleWriteLine(">").append(IOConstants.consoleReadLine())
		}.map { IO3<String> io1 ->
			io1.flatMap(convert())
		}
	}

	@TypeChecked
	void repl() {

		def w = IOConstants.consoleWriteLine(help)
		w.run()
		def s = stream().takeWhile { IO3<Boolean> io2 ->
			def b = !io2.run()
			b
		}
		s.toList()
	}

	void test2() {
		println Stream.range(1).takeWhile {
			it < 10
		}.toList()
	}

	static void main(def args) {
		def d = new IO3Demo()
//		d.test1()
//		d.test2()
		d.repl()
	}

}
