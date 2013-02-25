package perry.lazy

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

	def process(Closure yieldAction, List<Generator> gens, Map context) {

		def head = gens.head()
		def tail = gens.tail()
		if (tail.size() == 0) {
			def c = head.func
			c.setDelegate(context)
			c.resolveStrategy = Closure.DELEGATE_ONLY
			c.call().map { it ->
				context[head.name] = it
//				yieldAction.setDelegate(context + ["${head.name}": it])
				yieldAction.setDelegate(new Yield(values: context))
				yieldAction.resolveStrategy = Closure.DELEGATE_ONLY
				yieldAction.call()
			}
		} else {
//			A for comprehension for (p <-e;p0 <-e0 . . .) yield e00 ,
//					where . . . is a (possibly empty) sequence of generators, definitions, or guards,
//					is translated to
//			e.flatMap { case p => for (p0 <-e0 . . .) yield e00 } .
			def c = head.func
			c.setDelegate(context)
			c.resolveStrategy = Closure.DELEGATE_ONLY

			c.call().bind { it ->
				context[head.name] = it
				def r = process(yieldAction, tail, context)
				def temp = r.toJList()
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
