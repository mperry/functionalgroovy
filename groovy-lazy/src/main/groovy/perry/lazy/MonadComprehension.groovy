package perry.lazy

//import hr.helix.monadologie.monads.Monad
//import hr.helix.monadologie.mcategories.FunctorCategory
//import hr.helix.monadologie.mcategories.MReaderCategory
//import hr.helix.monadologie.mcategories.MMapCategory
//import hr.helix.monadologie.mcategories.MListCategory
//import hr.helix.monadologie.mcategories.MCollectionCategory
import fj.data.Stream

class MonadComprehension {

    // choosing the categories to be used on given monads

    private Class category(Collection c) { MCollectionCategory }

    private Class category(Range r) { MListCategory }

    private Class category(Map c) { MMapCategory }

    private Class category(Closure m) { MReaderCategory }

//    private Class category(Monad m) { FunctorCategory }

	private Class category(Stream s) { Stream }
    
    private Class category(Object o) {
        throw new IllegalArgumentException("Unsupported monad category: ${o.getClass().name}")
    }

    // ----- container storage -----

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

    // ----- comprehension implementation -----

    private inContext(String name, value, Closure action) {
        currProp[name] = value
        ctx(this, action)
    }

    private Closure ctx(delegate, Closure action) {
        action.delegate = delegate
        action.resolveStrategy = Closure.DELEGATE_ONLY
        action
    }

	private processOuter(List<String> names, Closure yieldAction) {
		processOuter1(names, yieldAction)
	}

	private processOuter2(List<String> names, Closure yieldAction) {
		def (curr, rest) = [names.head(), names.tail()]

		def currMonad = ctx(this, propVals[curr])()
//		def currMonad = ctx(this, propVals[curr])
		applyOptionalFn(currMonad)

		def streams = names.inject([]) { acc, it ->
			acc + propVals[it]
		}
		def last = streams.last()
		streams.inject(Stream.nil()) { acc, it ->
			if (it == last) {
				acc.bind({[]})
			}
		}

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
        comprehension.delegate = new MonadComprehension()
        comprehension.resolveStrategy = Closure.DELEGATE_ONLY
        comprehension()
    }
}
