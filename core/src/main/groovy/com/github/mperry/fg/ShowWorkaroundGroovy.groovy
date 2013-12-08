package com.github.mperry.fg

import com.gihub.mperry.ShowWorkaroundJava
import fj.F
import fj.Show
import fj.data.Stream
import fj.test.Arg

import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/12/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
class ShowWorkaroundGroovy {

	// Arg.argShow
	public static final Show<Arg<?>> argShowNullable() {
		return showS(new F<Arg<?>, String>() {
			public String f(final Arg<?> arg) {
				return ShowWorkaroundJava.anyShowNullable().showS(arg.value()) +
						(arg.shrinks() > 0 ? " (" + arg.shrinks() + " shrink" + (arg.shrinks() == 1 ? "" : 's') + ')' : "");
			}
		});
	}

	// Show.anyShow
	// does not compile with: "Groovyc unable to resolve class A"
//	public static <A> Show<A> anyShowNullable() {
//		F<A, Stream<Character>> f = new F<A, Stream<Character>>() {
//			public Stream<Character> f(final A a) {
//				return Stream.fromString((a == null) ? "null" : a.toString());
//			}
//		};
//		return Show.show(f);
//	}

}
