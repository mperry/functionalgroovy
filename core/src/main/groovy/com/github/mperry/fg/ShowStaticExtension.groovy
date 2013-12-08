package com.github.mperry.fg

import com.gihub.mperry.ShowTester
import fj.F
import fj.Show
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked

import static fj.Show.anyShow
import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/12/13
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class ShowStaticExtension {

//	public static <A> Option<A> unit(Option option, A value) {
//		Option.<A>some(value)
//	}
	public static <A> Show<A> anyShowNullable4() {
		Show.showS({A a ->
//			return Stream.fromString((a == null) ? "null" : a.toString());
			(a == null) ? "null" : a.toString()
		} as F<A, String>)
	}

//	public static <A> Show<A> anyShowNullable5() {
//		return new Show<A>(new F<A, Stream<Character>>() {
//			public Stream<Character> f(final A a) {
//				return Stream.fromString(a.toString());
//			}
//		});
//	}

	@TypeChecked
	public static <A> Show<A> anyShowNullable6() {
		return new Show<A>({A a ->
			Stream.fromString((a == null) ? "null" : a.toString())
		} as F<A, Stream<Character>>)

	}

	@TypeChecked
	public static <A> Show<A> anyShowNullable7() {
		ShowTester.anyShowNullable({A a ->
			Stream.fromString((a == null) ? "null" : a.toString())
		} as F<A, String>)

	}




//	public static <A> Show<A> anyShowNullable3() {
//		return new Show<A>(new F<A, Stream<Character>>() {
//			public Stream<Character> f(final A a) {
//				return Stream.fromString((a == null) ? "null" : a.toString());
//			}
//		});
//	}


}
