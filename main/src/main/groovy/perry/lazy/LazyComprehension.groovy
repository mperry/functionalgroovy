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
	private List<Generator> generators = []

	@TypeChecked
	def yield(Closure c) {
//		processInContext(c, generators, [:])
	    startStreamProcessing(c)
	}

	private def startStreamProcessing(Closure c) {
		def head = generators.head()
		processStream(c, generators.tail(), [:], executeGenerator(head.func, [:]), head.name)
	}

	@TypeChecked
	private Object executeFunction(Closure c, Object context) {
		c.setDelegate(context)
		c.resolveStrategy = Closure.DELEGATE_ONLY
		c.call()
	}

	private Object executeYield(Closure c, Yield context) {
		executeFunction(c, context)
	}

//	@TypeChecked
	private Stream<?> executeGenerator(Closure c, Object context) {
		executeFunction(c, context)
	}

//	@TypeChecked
	/**
	 * Process the generator list gen, looking ahead to create the context for the
	 * next action.  Does not support consecutive guards.  Use processStream instead.
	 * @deprecated
	 * @param yieldAction
	 * @param gens
	 * @param context
	 * @return
	 */
	private Stream<?> processInContext(Closure yieldAction, List<Generator> gens, Map context) {
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
					processInContext(yieldAction, tail, context + [(head.name): it])
				}
			} else {
				def s2 = s1.filter {
					executeFunction(tail.head().func, context + [(head.name): it])
				}
				if (tail.tail().size() == 0) {
					s2
				} else {
					s2.bind {
						processInContext(yieldAction, tail.tail(), context + [(head.name): it])
					}
				}
			}
		}
	}

	/**
	 * Process stream, when doing an action over stream, do so with
	 * lastVar in the context
	 * @param yieldAction
	 * @param gens
	 * @param context
	 * @param stream
	 * @param lastVar
	 * @return
	 */
	//	@TypeChecked
	private Stream<?> processStream(Closure yieldAction, List<Generator> gens, Map context, Stream stream, String lastVar) {

		if (gens.size() == 0) {
			stream.map {
				executeYield(yieldAction, new Yield(context + [(lastVar): it]))
			}
		} else {
			def head = gens.head()
			def tail = gens.tail()
			if (head.guard) {
				def s = stream.filter {
					executeFunction(head.func, context + [(lastVar): it])
				}
				processStream(yieldAction, tail, context, s, lastVar)
			} else {
				stream.bind {
					def c = context + [(lastVar): it]
					def s = executeGenerator(head.func, c)
					processStream(yieldAction, tail, c, s, head.name)
				}
			}
		}
	}

//	@TypeChecked
	private void methodMissing(String name, args) {
		generators << new Generator(name:  name, func: args[0], guard: (name == GUARD))
	}

//	@TypeChecked
	static Stream<?> foreach(Closure comprehension) {
		comprehension.delegate = new LazyComprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}

}
