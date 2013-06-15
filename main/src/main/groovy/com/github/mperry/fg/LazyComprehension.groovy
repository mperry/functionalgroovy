package com.github.mperry.fg

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
	def yield(Closure<?> c) {
	    startProcessing(c)
	}

	private def startProcessing(Closure<?> yieldClosure) {
		def head = generators.head()
		process(yieldClosure, generators.tail(), [:], executeGenerator(head.func, [:]), head.name)
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
	private def executeGenerator(Closure c, Object context) {
		executeFunction(c, context)
	}

	/**
	 * Process stream, when doing an action over stream, do so with
	 * lastVar in the context
	 */
	//	@TypeChecked
	private def process(Closure<?> yield, List<Generator> gs, Map<String, ?> context, def functorMonad, String lastVar) {
		if (gs.size() == 0) {
			functorMonad.map {
				executeYield(yield, new Yield(context + [(lastVar): it]))
			}
		} else {
			def head = gs.head()
			def tail = gs.tail()
			if (head.guard) {
				def s = functorMonad.filter {
					executeFunction(head.func, context + [(lastVar): it])
				}
				process(yield, tail, context, s, lastVar)
			} else {
				functorMonad.bind {
					def c = context + [(lastVar): it]
					process(yield, tail, c, executeGenerator(head.func, c), head.name)
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
		addGenerator(new Generator(name: name, func: args[0], guard: isGuard(name)))
	}

	Generator propertyMissing(String name) {
		def g = new Generator(name: name, guard: false)
		addGenerator(g)
		g
	}

	void addGenerator(Generator g) {
		generators << g
	}

//	@TypeChecked
	static def foreach(Closure comprehension) {
		comprehension.delegate = new LazyComprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}

}
