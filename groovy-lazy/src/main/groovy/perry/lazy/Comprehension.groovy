package perry.lazy

import fj.data.Stream
import fj.F2
import fj.F
import fj.Unit

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 24/02/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
class Comprehension {


	private Map<String, Closure> propVals = [:]
	private List<String> propNames = []
	private List<Closure> guards   = []
	private Map<String, Object> currProp = [:]

	private freeFunctions = []

	// ----- comprehension interface -----

	def takeFrom(Closure vals) { vals }

	def guard(Closure g) { guards << g }

	def yield(Closure yieldStep) {
		processOuter(propNames, yieldStep)
	}


	private processOuter(List<String> names, Closure yieldAction) {
		processOuter2(names, yieldAction)
	}

	private processOuter2(List<String> names, Closure yieldAction) {
		def (curr, rest) = [names.head(), names.tail()]

//		def currMonad = ctx(this, propVals[curr])()
		def currMonad = ctx(this, propVals[curr])
//		applyOptionalFn(currMonad)

		List<Stream> streams = names.inject([]) { List acc, it ->
			def s = propVals[it]
			def list = acc.add(s)
			acc
		}
		def h = streams.head()
		def tail = streams.tail()
		def bounded = tail.inject(h, {
			Stream acc, Stream it -> acc.combos(it)
		})

		def lists = bounded.fold([], {
//			List acc2, Stream<?> s ->
			def acc2, def s ->
			def list = s.toJList()
			def map = names.inject([0, [:]]){ acc, it ->
				def (index, map) = acc
				map[it] = list[index]
				[index + 1, map]
			}
			def m = map[1]
			def binding = new Binding(map[1])

			yieldAction.setDelegate(new Comprehension(currProp: m))
			yieldAction.resolveStrategy = Closure.DELEGATE_ONLY

			def x = yieldAction.call()
//			yield(propVals)
			acc2 + x
//			Unit.unit()
		})
		lists


	}



	private Closure ctx(delegate, Closure action) {
		action.delegate = delegate
		action.resolveStrategy = Closure.DELEGATE_ONLY
		action
	}

	private inContext(String name, value, Closure action) {
		currProp[name] = value
		ctx(this, action)
	}

	private processOuter1(List<String> names, Closure yieldAction) {
		def (curr, rest) = [names.head(), names.tail()]

		def currMonad = ctx(this, propVals[curr])()
		applyOptionalFn(currMonad)

		use(category(currMonad)) {
			if (rest)
				currMonad.bind { elem ->
					currProp[curr] = elem
					processOuter(rest, yieldAction)
				}
			else {
				def container = guards ? // filter if at least one guard is given
					currMonad.filter { elem ->
						guards.every { inContext(curr, elem, it)() }
					} : currMonad

				container.fmap { elem ->
					inContext(curr, elem, yieldAction)()
				}
			}
		}
	}

	/**
	 * Queries for potential calls to unbound functions that may (or may not)
	 * belong to the current monad. If such monad function is found, it is immediately
	 * applied to its arguments.
	 *
	 * @param monad the currently used monad
	 */
	private applyOptionalFn(monad) {
		if (freeFunctions) {
			def fnInfo = freeFunctions.head()
			/*try {
						 ... to catch the possible exception or not to catch: that is the question */
			monad."${fnInfo.fn}"(*fnInfo.args)
//                monad.metaClass.invokeMethod(monad, fnInfo.fn, fnInfo.args)
			freeFunctions.remove(0) // the function is used up, time for the next one
			/*}
						catch (MissingMethodException ignored) {
							// the method apparently doesn't belong to this monad, maybe next time
						}*/
		}
	}

	// ----- dynamic properties -----

	def propertyMissing(String name) {
		currProp[name]
	}

	def propertyMissing(String name, val) {
		propVals[name] = val
		propNames << name
	}

	def methodMissing(String name, args) {
		freeFunctions << [ fn:name, args:args ]
	}

	// ----- runner -----

	static foreach(Closure comprehension) {
		comprehension.delegate = new Comprehension()
		comprehension.resolveStrategy = Closure.DELEGATE_ONLY
		comprehension()
	}

}
