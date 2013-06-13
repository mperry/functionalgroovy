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
	def yield(Closure c) {
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

	/**
	 * Process stream, when doing an action over stream, do so with
	 * lastVar in the context
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
