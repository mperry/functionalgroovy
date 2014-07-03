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

    static <A, B> Option<B> flatMap(Option<A> option, F<A, Option<B>> f) {
        option.bind(f)
    }



}
