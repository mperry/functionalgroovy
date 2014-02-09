package com.github.mperry.fg.test

import fj.test.Arbitrary
import fj.test.Gen
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class ArbitraryStaticExtension {

	static Arbitrary<Integer> arbNullableInteger(Arbitrary a) {
        def list = fj.data.List.list(Gen.value(null), Arbitrary.arbInteger.gen)
        Arbitrary.arbitrary(Gen.oneOf(list))
	}

    static <A> Arbitrary<java.util.List<A>> arbJavaList(Arbitrary a, Arbitrary<A> aa) {
        ArbitraryCompanion.arbJavaList(aa)
    }

}
