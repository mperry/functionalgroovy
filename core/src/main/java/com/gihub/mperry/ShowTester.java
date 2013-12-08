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
	public static final Show<Arg<?>> argShowNullable() {
		return showS(new F<Arg<?>, String>() {
			public String f(final Arg<?> arg) {
				return anyShowNullable().showS(arg.value()) +
						(arg.shrinks() > 0 ? " (" + arg.shrinks() + " shrink" + (arg.shrinks() == 1 ? "" : 's') + ')' : "");
			}
		});
	}

	// Show.anyShow
	public static <A> Show<A> anyShowNullable() {
		F<A, Stream<Character>> f = new F<A, Stream<Character>>() {
			public Stream<Character> f(final A a) {
				return Stream.fromString((a == null) ? "null" : a.toString());
			}
		};
		return Show.show(f);
	}

}
