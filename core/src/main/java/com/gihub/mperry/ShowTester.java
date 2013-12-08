package com.gihub.mperry;

import fj.F;
import fj.Show;
import fj.data.Stream;
import fj.test.Arg;
import groovy.transform.TypeChecked;

import static fj.Show.anyShow;
import static fj.Show.showS;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/12/13
 * Time: 2:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShowTester<T> {

	// Arg.argShow
	public static final Show<Arg<?>> argShow() {
		return showS(new F<Arg<?>, String>() {
			public String f(final Arg<?> arg) {
				return anyShowNullable3().showS(arg.value()) +
						(arg.shrinks() > 0 ? " (" + arg.shrinks() + " shrink" + (arg.shrinks() == 1 ? "" : 's') + ')' : "");
			}
		});
	}

	public static <A> Show<A> anyShowNullable(F <A, String> f) {
		return Show.showS(f);
	}


	public static <A> Show<A> anyShowNullable() {
		return anyShowNullable(new F<A, String>(){
			@Override
			public String f(A a) {
				return (a == null) ? "null" : a.toString();
			}
		});

	}

//	public static <A> Show<A> anyShowNullable2() {
//		return new Show<A>(new F<A, Stream<Character>>() {
//			public Stream<Character> f(final A a) {
//				return Stream.fromString(a.toString());
//			}
//		});
//	}

	public static <A> Show<A> anyShowNullable3() {
//		return null;
		F<A, Stream<Character>> f = new F<A, Stream<Character>>() {
			public Stream<Character> f(final A a) {
//				return Stream.fromString("A");
				return Stream.fromString((a == null) ? "null" : a.toString());
			}
		};
		return Show.show(f);
	}

}
