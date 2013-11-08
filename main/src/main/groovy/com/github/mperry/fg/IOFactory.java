package com.github.mperry.fg;

import fj.F;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class IOFactory {

	public <A> IO3<A> unit(final A a) {
		return new IO3<A>() {
			public A run() {
				return a;
			}
		};
	}

	public <A, B> IO3<B> map(IO3<A> io, F<A, B> f) {
		return io.map(f);
	}

	public <A, B> IO3<B> flatMap(IO3<A> io, F<A, IO3<B>> f) {
		return io.flatMap(f);
	}

}
