package perry.lazy

import fj.data.Stream
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 26/02/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
class LazyComprehension {

	private static final String GUARD = "guard"
	List<Generator> generators = []

	@TypeChecked
	def yield(Closure c) {
		process(c, generators, [:])
	}

	@TypeChecked
	def execFunc(Closure c, Object context) {
		c.setDelegate(context)
		c.resolveStrategy = Closure.DELEGATE_ONLY
		c.call()
	}

	def process(Closure yieldAction, List<Generator> gens, Map context) {
		def head = gens.head()
		def tail = gens.tail()
		if (gens.size() == 1) {
			// v is Stream
			def z = execFunc(head.func, context)
			def v = z.map { it ->
				execFunc(yieldAction, new Yield(values: context + [(head.name): it]))
			}
			v
		} else {
			if (tail.head().guard) {
				// TODO
				def a = execFunc(head.func, context).filter({ it ->
					def c = context + [(head.name): it]
					def bool = execFunc(tail.head().func, c)
					bool

				})
				if (tail.tail().size() == 0) {
					a
				} else {
					a.bind({ it ->
						process(yieldAction, tail.tail(), context + [(head.name): it])
					})
				}



			} else {
				def a = execFunc(head.func, context)
				a.bind { it ->
					process(yieldAction, tail, context + [(head.name): it])
				}
			}
		}
	}

//	@TypeChecked
	void methodMissing(String name, args) {
		generators << new Generator(name:  name, func: args[0], guard: (name == GUARD))
	}

	@TypeChecked
	static def foreach(Closure comprehension) {
		comprehension.delegate = new LazyComprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}
}
