package perry.lazy

import fj.data.Stream
import fj.P
import fj.P2
import groovy.transform.TypeChecked
import fj.data.Option

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
class Comprehension {

	private Map<String, Object> variables = [:]
	private Option<Object> result = Option.none()
	List<Object> generators = []

//	A for comprehension for (p <-e;p0 <-e0 . . .) yield e00 ,
//	where . . . is a (possibly empty) sequence of generators, definitions, or guards,
//	is translated to
//	e.flatMap { case p => for (p0 <-e0 . . .) yield e00 } .
	void recursiveFor() {

		def g = generators.head()
		def tail = generators.tail()
		def firstVal = g.func.call()
		def method = tail.isEmpty() ? "values" : "bind"
		firstVal."${method}"({ it ->
//			call

			def closure = {
				foreach {

				}
			}
			closure.setDelegate(["${g.name}": it])

		})

		if (tail.isEmpty()) {

		}

	}

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
			yieldResultsAcc.add(yieldValue)
			yieldResultsAcc
		})
		yieldResults
	}

//	@TypeChecked
	void propertyMissing(String name, Object val) {
		variables[name] = val
//		if (result.isNone()) {
//			result = Option.some(val)
//		} else {
//			result = result.bind({Stream it -> it.combos(val)})
//		}
	}


	def methodMissing(String name, args) {
//		freeFunctions << [ fn:name, args:args ]
		def z = 0
//		if (result.isNone()) {
//			result = Option.some(val)
//		} else {
//			result = result.bind({Stream it -> it.combos(val)})
//		}
		generators << new Generator(name:  name, func: args)
	}

	@TypeChecked
	static List<?> foreach(Closure<List<?>> comprehension) {
		comprehension.delegate = new Comprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		(List<?>) comprehension.call()
	}

}
