package com.github.mperry.fg

import fj.data.Stream
import fj.F
import groovy.transform.TypeChecked
import fj.F2

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
class StreamExtension {

	@TypeChecked
	public static <T> Stream<T> filter(Stream<T> s, Closure<Boolean> c) {
		s.filter(c as F)
	}

//	@TypeChecked
	public static <T> Stream<T> findAll(Stream<T> s, Closure<Boolean> c) {
		s.filter(c)
	}

	@TypeChecked
	public static <A, B> Stream<B> map(Stream<A> s, Closure<B> c) {
		s.map(c as F)
	}

//	@TypeChecked
	public static <A, B> Stream<B> collect(Stream<A> s, Closure<B> c) {
		s.map(c)
	}

	@TypeChecked
	public static <A, B> Stream<B> bind(Stream<A> s, Closure<Stream<B>> c) {
		s.bind(c as F)
	}

	@TypeChecked
	public static <A, B> B fold(Stream<A> s, B initialValue, Closure<B> c) {
		s.foldLeft(c as F2, initialValue)
	}

	public static <A, B> B inject(Stream<A> s, B initialValue, Closure<B> c) {
		s.fold(initialValue, c)
	}

//	@TypeChecked
	public static <A> List<A> toJList(Stream<A> s) {
		s.map { A it ->
			def isRecursive = Stream.isInstance(it)
			if (isRecursive) {
				toJList((Stream<A>) it)
			} else {
				it
			}
		}.toCollection().toList()
	}

	public static <A> Stream<A> combos(Stream<A> s1, Stream<A> s2) {
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
