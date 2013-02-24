package perry.lazy.mcategories

/**
 * Category for Reader Monad.
 * Category is used to inject monad methods to closures.
 * Closures are best approximation of functions Groovy can give.
 * 
 * @author Dinko Srkoč
 * @since 2011-03-31
 */
class MReaderCategory extends FunctorCategory<Closure> {

    static Closure unit(Closure monad, elem) {
        { x -> elem }
    }

    static Closure bind(Closure monad, Closure f) {
        { w -> f(monad(w))(w) }
    }

    /*
       fmap is implemented here because the implementation from FunctorCategory
       has s problem. <code>unit</code> method inside bind closure is a free method
       and its context is not resolved to the closure monad.
     */
    static Closure fmap(Closure monad, Closure f) {
        monad.bind { a -> unit(monad, f(a)) }
    }
}
