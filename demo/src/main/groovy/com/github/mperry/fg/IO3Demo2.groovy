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
class IO3Demo2 {

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
	Option<IO3<Unit>> invalidMessageIO(String s) {
		invalidMessage(s).map { String it -> IOConstants.consoleWriteLine(it)}
	}

    IO3<Unit> invalidMessageIO2(String s) {
        invalidMessageIO(s).orSome(IO3.empty())
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

	void test1(Stream<IO3<String>> stream) {
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
    IO3<String> whole() {
        IOConstants.consoleReadLine().flatMap1({ String s ->
            invalidMessageIO2(s).append1(squareIO(s).orSome(IO3.empty()))
        } as F)
    }


    @TypeChecked
	IO3<?> recursiveInput(IO3<?> myIo) {
		def quit = "q"
		def io = IOConstants.consoleWriteLine(prompt)
		myIo.append(io).append(IOConstants.consoleReadLine().flatMap({ String s ->
				def f = foldIO([invalidMessageIO(s), squareIO(s)]).append(IO3.unit(s))

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
		def action = stream().foldLeft({ IO3<?> acc, IO3<?> io ->
			acc.append(io)
		} as F2<IO3, IO3, IO3>, IO3.empty())
		action.run()
//		stream().map { IO3 io ->
//			io.run()
//		}.toList()
	}



	IO3<?> repl3() {
		IOConstants.consoleWriteLine(help).append(recursiveInput(IO3.empty()))
	}

	void repl4() {
		def io = repl3()
		io.run()
	}

    @TypeChecked
    IO3<Stream<String>> seq() {
        println "hello world repl5"
        def w = whole()
//        Stream.iterateWhile({IO3 io -> io} as F, { IO3 io -> io.run() != "quit"} as F, w)
//        stream(w)
//        IO3.sequenceWhile(Stream.repeat(w), { String s -> s != quit } as F)
        IO3.sequenceWhile2(Stream.repeat(w), { String s -> s != quit } as F)


    }

    void repl5() {
        seq().run()

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
		def d = new IO3Demo2()
//		println "hello world"
		d.repl5()
	}

}
