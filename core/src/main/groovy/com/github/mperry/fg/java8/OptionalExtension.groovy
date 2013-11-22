package com.github.mperry.fg.java8

import fj.F

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
class OptionalExtension<A> {

	/**
	 * Sequence application, aka <*>, where (<*>) :: f (a -> b) -> f a -> f b
	 * @see http://hackage.haskell.org/package/base-4.2.0.0/docs/Control-Applicative.html
	 */
	def <B> Optional<B> apply(Optional<A> delegate, Optional<F<A, B>> of) {
		delegate.map { A a ->
			of.map { F<A, B> f ->
				Optional.of(f.f(a))
			}
		}
	}



}
