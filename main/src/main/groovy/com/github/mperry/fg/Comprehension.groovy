package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 26/02/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
class Comprehension {

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
	 * strucutre must have map, filter and bind implemented
	 */
	//	@TypeChecked
	private def process(Closure<?> yield, List<Generator> gs, Map<String, ?> context, def structure, String lastVar) {
		if (gs.size() == 0) {
			structure.map {
				executeYield(yield, new Yield(values: context + [(lastVar): it]))
			}
		} else {
			def head = gs.head()
			def tail = gs.tail()
			if (head.guard) {
				def s = structure.filter {
					def z = executeFunction(head.func, context + [(lastVar): it])
					z
				}
				process(yield, tail, context, s, lastVar)
			} else {
				structure.flatMap {
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
		comprehension.delegate = new Comprehension()
		comprehension.resolveStrategy = Closure.OWNER_FIRST
		comprehension.call()
	}

}
