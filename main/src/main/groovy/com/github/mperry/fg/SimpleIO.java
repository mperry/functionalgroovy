package com.github.mperry.fg;

import fj.F;
import fj.Unit;
import fj.control.parallel.Strategy;
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

	static public <B> SimpleIO<B> lift(final B b) {
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

}
