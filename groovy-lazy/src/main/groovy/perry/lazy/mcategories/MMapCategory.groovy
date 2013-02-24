package perry.lazy.mcategories

/**
 * This category is used to inject monad methods to Maps.
 *
 * @author Dinko Srkoč
 * @since 2011-03-17
 */
class MMapCategory extends FunctorCategory<Map> {
    static Map unit(Map map, Map elem) { elem.clone() }
    static Map unit(Map map, Map.Entry elem) { [:] << elem }
    static Map unit(Map map, key, value) { [(key):value] }
    static List unit(Map map, List list) { list.clone() } // idea: if list.size == 2 then [ list[0]:list[1] ]
    static List unit(Map map, value) { [value] }

    static def bind(Map map, Closure f) {
        map.inject([:]) { r, e ->
            def fRes = f(e)
            if (r in Map) {
                if (fRes in Map || fRes in Map.Entry)
                    r << fRes
                else // if one unmappable result found, transform whole result
                    r.collect { it } + fRes // into list
            }
            else
                r + fRes // r is Map or List
        }
    }

    static List bind(List map, Closure f) {
        map.inject([]) { r, e -> r + f(e) }
    }

    static Map filter(Map map, Closure f) { map.findAll(f) }

    static List fmap(Map map, Closure f) { map.collect { a -> f(a) }}
}
