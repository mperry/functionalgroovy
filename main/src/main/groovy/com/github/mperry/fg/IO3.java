package com.github.mperry.fg;

import fj.F;
import fj.Unit;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 7/11/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class IO3<A> {

    public abstract A run();

    public <B> IO3<B> append(final IO3<B> io) {
        return new IO3<B>() {
            public B run() {
                IO3.this.run();
                return io.run();
            }
        };
    }

	public <B> IO3<B> map(final F<A, B> f) {
		return new IO3<B>() {
			public B run() {
				return f.f(IO3.this.run());
			}
		};
	}


	public <B> IO3<B> flatMap(final F<A, IO3<B>> f) {
		return new IO3<B>() {
			public B run() {
				return f.f(IO3.this.run()).run();
			}
		};
	}


	static public <B> IO3<B> unit(final B b) {
		return new IO3<B>() {
			public B run() {
				return b;
			}
		};
	}

	static public IO3<Unit> empty() {
		return new IO3<Unit>() {
			public Unit run() {
				return Unit.unit();
			}
		};
	}



}
