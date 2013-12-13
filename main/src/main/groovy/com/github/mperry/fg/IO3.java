package com.github.mperry.fg;

import fj.F;
import fj.Unit;
import fj.data.Stream;
import groovy.lang.Closure;
import groovy.transform.TypeChecked;

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

    public <B> IO3<A> append1(final IO3<B> io) {
        return new IO3<A>() {
            @Override
            public A run() {
                A a = IO3.this.run();
                io.run();
                return a;
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

    public <B> IO3<A> flatMap1(final F<A, IO3<B>> f) {
        return new IO3<A>() {
            public A run() {
                A a = IO3.this.run();
                f.f(a).run();
                return a;
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

    static <B> IO3<Stream<B>> sequenceWhile(final Stream<IO3<B>> list, final F<B, Boolean> pred) {
        if (list.isEmpty()) {
            return IO3.unit(Stream.<B>nil());
        } else {
            IO3<B> h = list.head();
            final Stream<IO3<B>> t = list.tail()._1();
            return h.flatMap( new F<B, IO3<Stream<B>>>() {
                @Override
                public IO3<Stream<B>> f(B b) {
                    if (!pred.f(b)) {
                        return IO3.unit(Stream.<B>nil());
                    } else {
                        return IO3.unit(Stream.single(b)).append(sequenceWhile(t, pred));
                    }
                }
            });
        }
    }
}
