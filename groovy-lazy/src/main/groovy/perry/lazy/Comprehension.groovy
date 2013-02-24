package perry.lazy

import fj.data.Stream
import fj.P
import fj.P2
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
class Comprehension {

	private Map<String, Object> variables = [:]

	@TypeChecked
	List<?> yield(Closure yieldClosure) {
		def combined = createStreams(variables)
		createYieldResults(combined, variables.keySet().toList(), yieldClosure)
	}

//	@TypeChecked
	Stream<Stream<?>> createStreams(Map<String, Object> vars) {
		def list = (List<Stream<?>>) vars.keySet().inject([]) { List<Stream<?>> acc, String varName ->
			def b = acc.add((Stream) vars[varName])
			acc
		}
		def h = list.head()
		def tail = list.tail()
		(Stream<Stream<?>>) tail.inject(h, {Stream<?> acc, Stream<?> it ->
			acc.combos(it)
		})
	}

//	@TypeChecked
	List<?> createYieldResults(Stream<Stream<?>> combined, Collection<String> names, Closure<?> yieldClosure) {
		def yieldResults = combined.fold([], {List yieldResultsAcc, Stream<?> s ->
			def values = s.toJList()
			def varMap = names.inject(P.p(0, [:])){ P2<Integer, Map> acc, String varName ->
				def index = acc._1()
				def map  = acc._2()
				map[varName] = values[index]
				P.p(index + 1, map)
			}
			yieldClosure.setDelegate(new Yield(values: varMap._2()))
			yieldClosure.resolveStrategy = Closure.DELEGATE_ONLY
			def yieldValue = yieldClosure.call()
			yieldResultsAcc + yieldValue
		})
		yieldResults
	}

	@TypeChecked
	void propertyMissing(String name, Object val) {
		variables[name] = val
	}

	@TypeChecked
	static List<?> foreach(Closure<List<?>> comprehension) {
		comprehension.delegate = new Comprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		(List<?>) comprehension.call()
	}

}
