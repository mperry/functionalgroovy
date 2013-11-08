package com.github.mperry.fg;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class IOUnitFactory<A> {


	public IO3<A> unit(final A a) {
		return new IO3<A>() {
			public A run() {
				return a;
			}
		};
	}

}
