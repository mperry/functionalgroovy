package perry.lazy

import fj.data.Stream
import fj.data.Option

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 26/02/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
class LazyComprehension {

	List<Generator> generators = []

	def yield(Closure c) {
		process(c, generators, [:])
	}

	def execFunc(Closure c, Object context) {
		c.setDelegate(context)
		c.resolveStrategy = Closure.DELEGATE_ONLY
		def a = c.call()
//		def temp = a.toJList()
		a
	}

	def process(Closure yieldAction, List<Generator> gens, Map context) {

		def head = gens.head()
		def tail = gens.tail()
		if (gens.size() == 1) {
			// v is Stream
			def v = execFunc(head.func, context).map { it ->
				context[head.name] = it
				def d = execFunc(yieldAction, new Yield(values: context))
				d
			}
			v
		} else {
			execFunc(head.func, context).bind { it ->
				context[head.name] = it
				def r = process(yieldAction, tail, context)
				def temp = r.toJList()
//				Stream.stream(r)
				r
			}
		}
	}

	void methodMissing(String name, args) {
		generators << new Generator(name:  name, func: args[0])
	}

//	@TypeChecked
	static def foreach(Closure comprehension) {
		comprehension.delegate = new LazyComprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}
}
