package com.github.mperry.fg

import fj.F
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 12/06/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class CollectionExtension {

	static <A, B> List<B> map(Collection<A> collection, Closure<B> f) {
		collection.collect(f)
	}

    static <A, B> List<B> map(Collection<A> collection, F<A, B> f) {
        collection.collect(FExtension.toClosure(f))
    }

    static <A> Collection<A> filter(Collection<A> collection, F<A, Boolean> f) {
        collection.findAll(FExtension.toClosure(f))
    }

    static <A, B> B fold(Collection<A> collection, B initial, F<A, B> f) {
        collection.inject(initial, FExtension.toClosure(f))
    }

    static <A, B> List<B> flatMap(Collection<A> c, F<A, List<B>> f) {
        c.collectMany(FExtension.toClosure(f))
    }

    static <A> Boolean exists(Collection<A> c, F<A, Boolean> f) {
        findFirst(c, f).isSome()
    }

    static <A> Boolean forAll(Collection<A> c, F<A, Boolean> f) {
        !exists(c, { A a -> f.f(a) == false } as F)
    }

    static <A> Option<A> findFirst(Collection<A> c, F<A, Boolean> f) {
        Option.fromNull(c.find(FExtension.toClosure(f)))
    }

}
