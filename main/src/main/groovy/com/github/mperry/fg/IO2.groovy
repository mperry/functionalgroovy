package com.github.mperry.fg

abstract class IO2<A> {

	abstract A run()

//	def <B> IO2<B> append(IO2<B> io) {
//		new IO2<B>() {
//			B run(){
//				IO2.this.run()
//				io.run()
//			}
//		}
//	}

	def <B> IO2<B> append2(IO2<B> io) {
		[run: { ->
//			error
//			IO2.this.run()
			io.run()
		}] as IO2<B>
	}

//	def <B> IO2<B> map(F<A, B> f) {
//		return new IO2<B>() {
//			public B run() {
//				return f.f(IO2.this.run());
//			}
//		};
//	}
//
//	def <B> IO2<B> flatMap(F<A, IO2<B>> f) {
//		return new IO2<B>() {
//			public B run() {
//				return f.f(IO2.this.run()).run();
//			}
//		};
//	}


}
