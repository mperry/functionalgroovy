package com.github.mperry.fg;

import fj.F;
import fj.P1;
import fj.Unit;
import fj.control.Trampoline;
import fj.control.parallel.Strategy;
import fj.data.Stream;
import groovy.transform.TypeChecked;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                A a = SimpleIO.this.run();
				B b = f.f(a);
                return b;
			}
		};
	}


	public <B> SimpleIO<B> flatMap(final F<A, SimpleIO<B>> f) {
		return new SimpleIO<B>() {
			public B run() {
                A a = SimpleIO.this.run();
                SimpleIO<B> sb = f.f(a);
                B b = sb.run();
                return b;
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

	static public <B> SimpleIO<B> lift(final B b) {
		return new SimpleIO<B>() {
			public B run() {
				return b;
			}
		};
	}

	static public <B> SimpleIO<B> lift(final P1<B> p) {
		return new SimpleIO<B>() {
			public B run() {
				return p._1();
			}
		};
	}

    public SimpleIO<groovyx.gpars.dataflow.Promise<A>> gparsPromise() {
        final SimpleIO<A> self = this;
        return new SimpleIO<groovyx.gpars.dataflow.Promise<A>>() {
            @Override
            public groovyx.gpars.dataflow.Promise<A> run() {
                return SimpleIOExtension.asyncGpars(self);
            }
        };
    }

    public SimpleIO<fj.control.parallel.Promise<A>> fjPromise() {
        final SimpleIO<A> self = this;
        return new SimpleIO<fj.control.parallel.Promise<A>>() {
            @Override
            public fj.control.parallel.Promise<A> run() {
                return SimpleIOExtension.asyncFj(self);
            }
        };
    }

    public SimpleIO<Future<A>> future() {
        // the service needs to be shutdown or the program will not terminate
        return future(defaultService());
    }

    public SimpleIO<Future<A>> future(final ExecutorService service) {
        final SimpleIO<A> self = this;
        return new SimpleIO<Future<A>>() {
            @Override
            public Future<A> run() {
                return SimpleIOExtension.asyncJava(self, service);
            }
        };
    }

    public static Strategy<Unit> defaultStrategy() {
        return Strategy.<Unit>simpleThreadStrategy();
    }

    public static ExecutorService defaultService() {
        // the service needs to be shutdown or the program will not terminate
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public static <A> Trampoline<SimpleIO<Stream<A>>> transform(final SimpleIO<Trampoline<SimpleIO<Stream<A>>>> io) {
        SimpleIO<Stream<A>> io2 = new SimpleIO<Stream<A>>() {
              @Override
              public Stream<A> run() {
                  return io.run().run().run();
              }
        };
        return Trampoline.pure(io2);
    }

    static <A> SimpleIO<Stream<A>> sequenceWhile(final Stream<SimpleIO<A>> stream, final F<A, Boolean> f) {
        return new SimpleIO<Stream<A>>() {
            @Override
            public Stream<A> run() {
                boolean loop = true;
                Stream<SimpleIO<A>> input = stream;
                Stream<A> result = Stream.<A>nil();
                while (loop) {
                    if (input.isEmpty()) {
                        loop = false;
                    } else {
                        A a = input.head().run();
                        if (!f.f(a)) {
                            loop = false;
                        } else {
                            input = input.tail()._1();
                            result = result.cons(a);
                        }
                    }
                }
                return result.reverse();
            }
        };
    }

}
