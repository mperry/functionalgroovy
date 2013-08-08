package com.github.mperry.fg.test

import fj.F
import fj.Show
import fj.data.Stream
import fj.test.Arg
import fj.test.CheckResult

import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
class CheckResultStaticExtension {

	static Show<CheckResult> summaryNullable() {
		CheckResult.summary(argShowNullable())
	}

	static Show<Arg<?>> argShowNullable() {
		showS(new F<Arg<?>, String>() {
			public String f(final Arg<?> arg) {
				return anyShowNullable().showS(arg.value) +
						(arg.shrinks > 0 ? " (" + arg.shrinks + " shrink" + (arg.shrinks == 1 ? "" : 's') + ')' : "");
			}
		});
	}

	static <A> Show<A> anyShowNullable() {
		def c = { def a ->
			Stream.fromString(a == null ? "null" : a.toString())
		}
		new Show<A>(c as F<A, Stream<Character>>);
	}
}
