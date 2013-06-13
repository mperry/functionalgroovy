package com.github.mperry.fg

import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 30/11/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
class OptionStaticExtension {

//	@TypeChecked
	public static <A> Option<A> unit(Option option, A value) {
		Option.<A>some(value)
	}

}
