package com.github.mperry.fg

import fj.data.Option
import fj.data.Either
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 30/11/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class EitherExtension {

	/**
	 * Binds over the right value
	 */
	@TypeChecked
	public static <A, B, C> Either<A, C> bind(Either<A, B> either, Closure<Either<A, C>> f) {
		either.isLeft() ?
			Either.<A, C>left(either.left().value()) :
			(Either<A, C>) f.call(either.right().value())
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	public static <A, B, C> Either<A, C> collectMany(Either<A, B> either, Closure<Either<A, C>> f) {
		either.bind(f)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B, C> Either<A, C> map(Either<A, B> either, Closure<C> f) {
		either.isLeft() ?
			Either.<A, C>left(either.left().value()) :
			Either.<A, C>right(f.call(either.right().value()))
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B, C> Either<A, C> collect(Either<A, B> either, Closure<C> f) {
		either.map(f)
	}

	/**
	 * Same as Validation.filter.  Returns None if this is a failure or if the given
	 * predicate f does not hold for the success value, otherwise, returns a success in Some.
	 * @param e
	 * @param f
	 * @return
	 */
    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> Option<Either<A, B>> filterEither(Either<A, B> e, Closure<Boolean> f) {
		e.isLeft() ?
			Option.none() :
			f.call(e.right().value()) ? Option.some(e) : Option.<Either<A, B>>none()
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> Either<A, Option<B>> filterRight(Either<A, B> e, Closure<Boolean> f) {
		e.isLeft() ?
			Either.<A, Option<B>>left(e.left().value()) :
			Either.right(
				f.call(e.right().value()) ? Option.some(e.right().value()) : Option.<B>none()
			)
	}

	/**
	 * Copies the FunctionalJava Either.filter and Validation.filter method, implemented as filterEither
	 * @param e
	 * @param f
	 * @return
	 */
    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> Option<Either<A, B>> filter(Either<A, B> e, Closure<Boolean> f) {
		e.filterEither(f)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> Option<Either<A, B>> findAll(Either<A, B> e, Closure<Boolean> f) {
		e.filter(f)
	}

}
