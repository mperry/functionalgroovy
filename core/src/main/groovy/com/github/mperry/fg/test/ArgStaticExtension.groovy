package com.github.mperry.fg.test

import com.gihub.mperry.ShowTester
import com.github.mperry.fg.ShowStaticExtension
import fj.F
import fj.Show
import fj.test.Arg
import groovy.transform.TypeChecked

import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/12/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
class ArgStaticExtension {

//	@TypeChecked
//	static Show<Arg<?>> argShowNullable(Arg a) {
//		showS(new F<Arg<?>, String>() {
//			public String f(final Arg<?> arg) {
//				def sh1 = ShowStaticExtension.anyShowNullable4()
//				def sh2 = ShowTester.anyShowNullable()
//				// TODO: mperry
//				// TODO: this seems to call showS(F<A, B>) not showS(A)
//				def s = sh2.showS(arg.value)
//				def shrinkInfo = (arg.shrinks > 0 ? " (" + arg.shrinks + " shrink" + (arg.shrinks == 1 ? "" : 's') + ')' : "");
//				def result = s + shrinkInfo
//				result
//			}
//		});
//	}

//	static <A> Show<A> anyShowNullable(Arg arg) {
//		Show.showS({ A a -> (a == null) ? "null" : a.toString()} as F<A, String>);
//	}

}
