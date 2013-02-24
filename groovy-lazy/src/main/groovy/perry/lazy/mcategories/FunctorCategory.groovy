package perry.lazy.mcategories

class FunctorCategory<M> {

    static M fmap(M m, Closure f) { m.bind { a -> m.unit(f(a)) }}

}
