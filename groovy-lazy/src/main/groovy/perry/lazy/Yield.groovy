package perry.lazy

import groovy.transform.TypeChecked
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
@Canonical
class Yield {

	private Map<String, Object> values = [:]
	Closure closure

	Object propertyMissing(String name) {
		def v = this.values[name]
		def b = values[name]
		b
	}

}
