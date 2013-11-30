package com.github.mperry.fg.test

import fj.P2
import fj.test.CheckResult
import fj.test.Property
import fj.test.reflect.Check
import fj.data.List
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 30/11/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class Reflect extends GroovyTestCase {

	Property test1Prop() {
		new DbcProperties().test1Prop()
	}

	void test1() {

		Check.check(List.list(DbcProperties.class)).each { P2<String, CheckResult> p2 ->
			def cr = p2._2()
			CheckResult.summary.println(cr)
			assertTrue(cr.isPassed() || cr.isProven())
		}
	}

}
