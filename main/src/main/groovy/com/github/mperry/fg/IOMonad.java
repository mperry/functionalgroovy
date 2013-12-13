package com.github.mperry.fg;

import fj.F;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class IOMonad<A> {

	public SimpleIO<A> unit(final A a) {
		return new SimpleIO<A>() {
			public A run() {
				return a;
			}
		};
	}

	public <B> SimpleIO<B> map(SimpleIO<A> io, F<A, B> f) {
		return io.map(f);
	}

	public <B> SimpleIO<B> flatMap(SimpleIO<A> io, F<A, SimpleIO<B>> f) {
		return io.flatMap(f);
	}

}
