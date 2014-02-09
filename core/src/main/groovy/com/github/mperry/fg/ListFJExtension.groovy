package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: mwp
 * Date: 3/12/13
 * Time: 9:49 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class ListFJExtension {

    static <A> java.util.List<A> toJavaList(fj.data.List<A> list) {
        new ArrayList<A>(list.toCollection())
    }

}
