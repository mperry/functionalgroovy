package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 30/11/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class OptionStaticExtension {

    @TypeChecked(TypeCheckingMode.SKIP)
	public static <A> Option<A> unit(Option option, A value) {
		Option.<A>some(value)
	}

	public static <A, B> F<Option<A>, Option<B>> liftM1(Option o, F<A, B> f) {
		{ Option<A> oa, Option<B> ob ->
			oa.map({ A a ->
				f.f(a)
			} as F)
		} as F
	}

	public static <A, B, C> F2<Option<A>, Option<B>, Option<C>> liftM2(Option o, F2<A, B, C> f) {
		{
			Option<A> oa, Option<B> ob ->
				oa.bind({ A a ->
					ob.map({ B b ->
						f.f(a, b)
					} as F)
				} as F)
		} as F2
	}

	public static <A, B, C, D> F3<Option<A>, Option<B>, Option<C>, Option<D>> liftM3(Option o, F3<A, B, C, D> f) {
		{
			Option<A> oa, Option<B> ob, Option<C> oc ->
				oa.bind({ A a ->
					ob.bind({ B b ->
						oc.map({ C c ->
							f.f(a, b, c)
						} as F)
					} as F)
				} as F)
		} as F3
	}

}
