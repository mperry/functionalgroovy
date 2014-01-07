package com.github.mperry.fg

import fj.data.Option
import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 30/11/12
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class OptionExtension {

	/**
	 * (M a) -> (a -> M b) -> (M b)
	 */
	public static <A, B> Option<B> bind(Option<A> option, Closure<Option<B>> f) {
		option.bind(f as F)
	}

	static <A, B> Option<B> collectMany(Option<A> option, Closure<Option<B>> f) {
        bind(option, f)
	}

	public static <A, B> Option<B> map(Option<A> option, Closure<B> f) {
		option.map(f as F)
	}

	static <A, B> Option<B> collect(Option<A> option, Closure<B> f) {
        map(option, f)
	}

	static <A> Option<A> filter(Option<A> option, Closure<Boolean> f) {
		option.filter(f as F)
	}

	static <A> Option<A> findAll(Option<A> option, Closure<Boolean> f) {
        filter(option, f)
	}

}
