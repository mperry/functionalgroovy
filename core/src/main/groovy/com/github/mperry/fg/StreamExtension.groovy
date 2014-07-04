package com.github.mperry.fg

import fj.data.Stream
import fj.F
import groovy.transform.TypeChecked
import fj.F2
import groovy.transform.TypeCheckingMode

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class StreamExtension {

    static <A, B> Stream<B> flatMap(Stream<A> s, F<A, Stream<B>> f) {
        s.bind(f)
    }

	static <A> List<A> toJList(Stream s) {
		s.map { Object it ->
			def isRecursive = Stream.isInstance(it)
			if (isRecursive) {
				toJList((Stream) it)
			} else {
				it
			}
		}.toCollection().toList()
	}

    static <A> List<A> toJavaList(Stream<A> s) {
		new ArrayList<A>(s.toCollection())
    }

    @TypeChecked(TypeCheckingMode.SKIP)
	static <A> Stream<A> combos(Stream<A> s1, Stream<A> s2) {
		s1.bind { A s1Val ->
			s2.map { A s2Val ->
				if (Stream.isInstance(s1Val)) {
					Stream s1Stream = (Stream) s1Val
					s1Stream.append(Stream.stream(s2Val))
				} else {
					Stream.stream(s1Val, s2Val)
				}
			}
		}
	}

}
