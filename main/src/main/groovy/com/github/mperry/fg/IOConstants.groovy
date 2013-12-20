package com.github.mperry.fg;

import fj.Unit
import fj.data.Option
import groovy.transform.TypeChecked;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class IOConstants {

	@TypeChecked
	static Option<Console> console() {
		Option.fromNull(System.console())
	}

	public static SimpleIO<String> consoleReadLine() {
		return new SimpleIO<String>() {
			public String run() {
				def i = System.in
				def r = i.newReader()
				r.readLine()
//				i.withReader {
//					it.readLine()
//				}
			}
		};
	}

	@TypeChecked
	public static SimpleIO<Option<String>> consoleReadLineOption() {
		return new SimpleIO<Option<String>>() {
			public Option<String> run() {
				Option.fromNull(System.in.withReader {
					it.readLine()
				})
//				console().map { it.readLine() }
			}
		};
	}

	@TypeChecked
	public static SimpleIO<Unit> consoleWriteLine(final String msg) {
		return new SimpleIO<Unit>() {
			public Unit run() {
				println(msg)
				return Unit.unit();
			}
		};
	}


}
