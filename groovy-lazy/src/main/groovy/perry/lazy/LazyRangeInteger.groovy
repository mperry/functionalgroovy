package perry.lazy

import groovy.transform.Canonical
import fj.data.Enumerator

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 18/12/12
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Canonical
class LazyRangeInteger implements Comparable {

	Integer value
	def e = Enumerator.intEnumerator

	@Override
	int compareTo(Object other) {
		value.compareTo(((LazyRangeInteger) other).value)
	}

	@Override
	LazyRangeInteger next() {
		new LazyRangeInteger(e.successor(value).some())
	}

	@Override
	LazyRangeInteger previous() {
		new LazyRangeInteger(e.predecessor(value).some())
	}


}
