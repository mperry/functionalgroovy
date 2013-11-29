package com.github.mperry.fg.test

import fj.data.Option
import org.junit.Assert
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 5/08/13
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
class MyTest {


	@Test
	void test1() {
		println "test1"
		MyTest.metaClass.static.m1 = { -> m2() }
		MyTest.metaClass.static.option = { def value ->
			println "option2"
			Option.some(value)
		}
		def s = m1()
		def s2 = option(1)
		println s
		println s2
		Assert.assertTrue(s == "m2")
	}

	static String m1() {
		"m1"
	}

	static String m2() {
		"m2"
	}

	public static <A> Option<A> option(A value) {
		println "option1"
		Option.some(value)
	}


}
