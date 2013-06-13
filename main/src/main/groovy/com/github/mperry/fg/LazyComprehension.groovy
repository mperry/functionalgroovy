package com.github.mperry.fg

import fj.data.Stream

import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 26/02/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
class LazyComprehension<A> {

	private static final String GUARD = "guard"
	private List<Generator> generators = []

	@TypeChecked
	Stream<A> yield(Closure<?> c) {
	    startStreamProcessing(c)
	}

	private Stream<A> startStreamProcessing(Closure<?> yieldClosure) {
		def head = generators.head()
		processStream(yieldClosure, generators.tail(), [:], executeGenerator(head.func, [:]), head.name)
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

	/**
	 * Process stream, when doing an action over stream, do so with
	 * lastVar in the context
	 */
	//	@TypeChecked
	private Stream<?> processStream(Closure<?> yield, List<Generator> gs, Map<String, ?> context, Stream<?> stream, String lastVar) {
		if (gs.size() == 0) {
			stream.map {
				executeYield(yield, new Yield(context + [(lastVar): it]))
			}
		} else {
			def head = gs.head()
			def tail = gs.tail()
			if (head.guard) {
				def s = stream.filter {
					executeFunction(head.func, context + [(lastVar): it])
				}
				processStream(yield, tail, context, s, lastVar)
			} else {
				stream.bind {
					def c = context + [(lastVar): it]
					processStream(yield, tail, c, executeGenerator(head.func, c), head.name)
				}
			}
		}
	}

	@TypeChecked
	Boolean isGuard(String name) {
		name == GUARD
	}

//	@TypeChecked
	private void methodMissing(String name, args) {
		generators << new Generator(name: name, func: args[0], guard: isGuard(name))
	}

//	@TypeChecked
	static <A> Stream<A> foreach(Closure<Stream<A>> comprehension) {
		comprehension.delegate = new LazyComprehension<A>()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}

}
