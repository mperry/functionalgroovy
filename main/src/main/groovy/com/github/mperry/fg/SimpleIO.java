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
public abstract class SimpleIO<A> {

    public abstract A run();

    public <B> SimpleIO<B> append(final SimpleIO<B> io) {
        return new SimpleIO<B>() {
            @Override
            public B run() {
                SimpleIO.this.run();
                return io.run();
            }
        };
    }

    public <B> SimpleIO<A> append1(final SimpleIO<B> io) {
        return new SimpleIO<A>() {
            @Override
            public A run() {
                A a = SimpleIO.this.run();
                io.run();
                return a;
            }
        };
    }

	public <B> SimpleIO<B> map(final F<A, B> f) {
		return new SimpleIO<B>() {
			public B run() {
				return f.f(SimpleIO.this.run());
			}
		};
	}


	public <B> SimpleIO<B> flatMap(final F<A, SimpleIO<B>> f) {
		return new SimpleIO<B>() {
			public B run() {
				return f.f(SimpleIO.this.run()).run();
			}
		};
	}

    public <B> SimpleIO<A> flatMap1(final F<A, SimpleIO<B>> f) {
        return new SimpleIO<A>() {
            public A run() {
                A a = SimpleIO.this.run();
                f.f(a).run();
                return a;
            }
        };
    }

	static public <B> SimpleIO<B> unit(final B b) {
		return new SimpleIO<B>() {
			public B run() {
				return b;
			}
		};
	}

	static public SimpleIO<Unit> empty() {
		return new SimpleIO<Unit>() {
			public Unit run() {
				return Unit.unit();
			}
		};
	}

}
