package com.github.mperry.fg
/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 12/06/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
class CollectionExtension {

	public static <B> List<B> map(Collection<?> collection, Closure<B> f) {
		collection.collect(f)
	}

	public static <A> Collection<A> filter(Collection<A> collection, Closure<Boolean> f) {
		collection.findAll(f)
	}

	public static <A, B> B fold(Collection<A> collection, B initial, Closure<B> f) {
		collection.inject(initial, f)
	}

	public static <A, B> List<B> bind(Collection<A> c, Closure<List<B>> f) {
		c.collectMany(f)
	}

	public static <A> Boolean exists(Collection<A> c, Closure<Boolean> f) {
		c.find(f) != null
	}

	public static <A> Boolean forAll(Collection<A> c, Closure<Boolean> f) {
		!c.exists {
			f(it) == false
		}
	}

}
