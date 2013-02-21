package perry.groovy.lazy

import fj.data.Stream
import fj.F
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 16/02/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class StreamExtension {

	public static <T> Stream<T> filter(Stream<T> s, Closure<Boolean> c) {
		s.filter(c as F)
	}

	public static <T> Stream<T> map(Stream<T> s, Closure<Boolean> c) {
		s.map(c as F)
	}

	public static <A, B> Stream<B> bind(Stream<A> s, Closure<Stream<B>> c) {
		s.bind(c as F)
	}

	public static <A> List<A> toJList(Stream<A> s) {
		s.map({ A it ->
			def isRecursive = Stream.isInstance(it)
			if (isRecursive) {
				toJList((Stream<A>) it)
			} else {
				it
			}
		} as F).toCollection().toList()
	}

	public static <A> Stream<Stream<A>> combos1(Stream<A> s1, Stream<A> s2) {
		s1.bind({ A a ->
			s2.map({ A b ->
				Stream.stream(a, b)
			})
		})
	}

	public static <A> Stream<A> combos(Stream<A> s1, Stream<A> s2) {
		s1.bind({ A a ->
			s2.map({ A b ->
				if (Stream.isInstance(a)) {
					Stream c = (Stream) a
					c.append(Stream.stream(b))
				} else {
					Stream.stream(a, b)
				}
			})
		})
	}





}
