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
//@TypeChecked
@Canonical
class Yield {

	private Map values = [:]
	Closure closure

	Object propertyMissing(String name) {
		def found = values.containsKey(name)
		Set k = values.keySet()
		def f = k.first()
		def c = name.toString()
		def found2 = values.containsKey(c)
		def b3 = f == name
		def v2 = values[c]
		def v = this.values[name]
		def b = values[name]
		b
	}

}
