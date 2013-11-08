package com.github.mperry.fg;

import fj.Unit;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
class IOConstants {


	public static IO3<String> consoleReadLine() {
		return new IO3<String>() {
			public String run() {
				return System.console().readLine();
			}
		};
	}
//
	public static IO3<Unit> consolePrintLine(final String msg) {
		return new IO3<Unit>() {
			public Unit run() {
				System.console().printf("%s", msg);
				return Unit.unit();
			}
		};
	}


	public static <A> IO3<A> unit(final A a) {
		return new IO3<A>() {
			public A run() {
				return a;
			}
		};
	}

}
