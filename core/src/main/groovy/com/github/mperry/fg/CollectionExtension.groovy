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

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> List<B> map(Collection<A> collection, Closure<B> f) {
		collection.collect(f)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A> Collection<A> filter(Collection<A> collection, Closure<Boolean> f) {
		collection.findAll(f)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A> Collection<A> filter(Collection<A> collection, F<A, Boolean> f) {
        filter(collection, f.toClosure())
    }

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A, B> B fold(Collection<A> collection, B initial, Closure<B> f) {
        (B) collection.inject(initial, f)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> B fold(Collection<A> collection, B initial, F<A, B> f) {
        fold(collection, f.toClosure())
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A, B> List<B> flatMap(Collection<A> c, Closure<List<B>> f) {
        c.collectMany(f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A, B> List<B> flatMap(Collection<A> c, F<A, List<B>> f) {
        flatMap(c, f.toClosure())
    }

    @TypeChecked(TypeCheckingMode.SKIP)
	public static <A> Boolean exists(Collection<A> c, Closure<Boolean> f) {
        exists(c, f as F)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A> Boolean exists(Collection<A> c, F<A, Boolean> f) {
        findFirst(c, f).isSome()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A> Boolean forAll(Collection<A> c, F<A, Boolean> f) {
        !exists(c, { A a -> f.f(a) == false } as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A> Boolean forAll(Collection<A> c, Closure<Boolean> f) {
        forAll(c, f as F)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	public static <A> Option<A> findFirst(Collection<A> c, Closure<Boolean> f) {
		Option.fromNull(c.find(f))
	}

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A> Option<A> findFirst(Collection<A> c, F<A, Boolean> f) {
        findFirst(c, f.toClosure())
    }

}
