package com.github.mperry.fg

import fj.data.Option
import fj.data.Either
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 30/11/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
class EitherExtension {

	@TypeChecked
	public static <A, B, C> Either<A, C> bind(Either<A, B> either, Closure<Either<A, C>> f) {
		either.isLeft() ?
			Either.<A, C>left(either.left().value()) :
			(Either<A, C>) f.call(either.right().value())
	}

}
