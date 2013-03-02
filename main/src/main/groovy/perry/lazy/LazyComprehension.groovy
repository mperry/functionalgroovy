package perry.lazy

import fj.data.Stream

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
	Object executeFunction(Closure c, Object context) {
		c.setDelegate(context)
		c.resolveStrategy = Closure.DELEGATE_ONLY
		c.call()
	}

	Object executeYield(Closure c, Yield context) {
		executeFunction(c, context)
	}

//	@TypeChecked
	Stream<?> executeGenerator(Closure c, Object context) {
		executeFunction(c, context)
	}

//	@TypeChecked
	Stream<?> process(Closure yieldAction, List<Generator> gens, Map context) {
		def head = gens.head()
		def tail = gens.tail()
		if (gens.size() == 1) {
			executeGenerator(head.func, context).map {
				executeYield(yieldAction, new Yield(context + [(head.name): it]))
			}
		} else {
			def s1 = executeGenerator(head.func, context)
			if (!tail.head().guard) {
				s1.bind {
					process(yieldAction, tail, context + [(head.name): it])
				}
			} else {
				def s2 = s1.filter {
					executeFunction(tail.head().func, context + [(head.name): it])
				}
				if (tail.tail().size() == 0) {
					s2
				} else {
					s2.bind {
						process(yieldAction, tail.tail(), context + [(head.name): it])
					}
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
