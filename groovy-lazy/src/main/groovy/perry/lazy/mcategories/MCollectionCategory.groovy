package perry.lazy.mcategories

/**
 * This category is used to inject monad methods to collections.
 *
 * @author Dinko Srkoč
 * @since 2011-03-17
 */
class MCollectionCategory extends FunctorCategory<Collection> {
    static Collection unit(Collection coll, elem) {
        coll.getClass().newInstance() << elem
    }

    static Collection bind(Collection coll, Closure f) {
        coll.inject(coll.getClass().newInstance()) { r, e -> r + f(e) }
    }

    static Collection filter(Collection coll, Closure f) {
        coll.findAll(f)
    }
}

