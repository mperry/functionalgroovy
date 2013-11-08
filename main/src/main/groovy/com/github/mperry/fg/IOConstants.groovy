package com.github.mperry.fg;

import fj.Unit
import fj.data.Option;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
class IOConstants {

	static Option<Console> console() {
		Option.fromNull(System.console())
	}

	public static IO3<Option<String>> consoleReadLine() {
		return new IO3<Option<String>>() {
			public Option<String> run() {
				Option.fromNull(System.in.withReader {
					it.readLine()
				})
//				console().map { it.readLine() }
			}
		};
	}

	public static IO3<Unit> consoleWriteLine(final String msg) {
		return new IO3<Unit>() {
			public Unit run() {
//				console().map {
//					it.printf("%s", msg)
//					it
//				}
				println(msg)
//				System.console().printf("%s", msg);
				return Unit.unit();
			}
		};
	}


}
