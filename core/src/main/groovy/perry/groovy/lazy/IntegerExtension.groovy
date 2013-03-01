package perry.groovy.lazy

import fj.data.Either
import fj.data.Stream
import fj.data.Enumerator
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 19/12/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class IntegerExtension {

	/**
	 * @param from Start from (inclusive)
	 * @param to End at (inclusive)
	 * @return
	 */
	public static Stream<Integer> to(Integer from, Integer to) {
		Stream.range(from, to + 1)
	}
}
