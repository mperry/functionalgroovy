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
class LazyComprehension {

	private static final String GUARD = "guard"
	private List<Generator> generators = []

	@TypeChecked
	def yield(Closure<?> c) {
	    startStreamProcessing(c)
	}

	private def startStreamProcessing(Closure<?> yieldClosure) {
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
	private def executeGenerator(Closure c, Object context) {
		executeFunction(c, context)
	}

	/**
	 * Process stream, when doing an action over stream, do so with
	 * lastVar in the context
	 */
	//	@TypeChecked
	private def processStream(Closure<?> yield, List<Generator> gs, Map<String, ?> context, def functorMonad, String lastVar) {
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
				processStream(yield, tail, context, s, lastVar)
			} else {
				functorMonad.bind {
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
		def z = 0
//		generators << new Generator(name: name, func: args[0], guard: isGuard(name))
		addGenerator(new Generator(name: name, func: args[0], guard: isGuard(name)))
	}

	void addGenerator(Generator g) {
		generators << g
	}

	def propertyMissing(String name, value) {
		def z = 0
	}

	def propertyMissing(String name) {
		def z = 0
//		generators << new Generator(name: name, guard: false)
		def g = new Generator(name: name, guard: false)
		addGenerator(g)
		g

	}

//	@TypeChecked
	static def foreach(Closure comprehension) {
		comprehension.delegate = new LazyComprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension.call()
	}

}
