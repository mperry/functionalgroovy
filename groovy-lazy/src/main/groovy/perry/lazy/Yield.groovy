package perry.lazy

import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class Yield {

	private Map<String, Object> values = [:]

	Object propertyMissing(String name) {
		values[name]
	}

}
