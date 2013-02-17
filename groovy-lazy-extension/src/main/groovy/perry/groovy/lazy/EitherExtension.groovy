package perry.groovy.lazy

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
//@TypeChecked
class EitherExtension {

	/**
	 * (M t) -> (t -> M u) -> (M u)
	 * @param either
	 * @param c
	 * @return
	 */
	public static <A, B, C> Either<A, C> bind(Either<A, B> either, Closure<Either<A, C>> f) {

		either.isLeft() ?
			Either.<A, C>left(either.left().value()) :
			f.call(either.right().value())
	}




}
