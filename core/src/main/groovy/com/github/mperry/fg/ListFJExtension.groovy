package com.github.mperry.fg

/**
 * Created with IntelliJ IDEA.
 * User: mwp
 * Date: 3/12/13
 * Time: 9:49 AM
 * To change this template use File | Settings | File Templates.
 */
class ListFJExtension {

    static <A> fj.data.List<A> toJavaList(fj.data.List<A> list) {
        list.toCollection().toList()
    }

}
