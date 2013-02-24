package perry.lazy.mcategories

/**
 * This category is used to inject monad methods to Ranges. <br>
 * List is easily processed under Collection, but Range isn't.
 * Range, however, can be processed as List.
 *
 * @author Dinko Srkoč
 * @since 2011-03-22
 */
class MListCategory extends FunctorCategory<List> {
    static List unit(List list, elem) {
        [elem]
    }

    static List bind(List list, Closure f) {
        list.inject([]) { r, e -> r + f(e) }
    }

    static List filter(List list, Closure f) {
        list.findAll(f)
    }
}

