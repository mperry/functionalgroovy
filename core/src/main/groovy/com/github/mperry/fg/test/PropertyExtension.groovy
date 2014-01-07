package com.github.mperry.fg.test

import fj.Show
import fj.Unit
import fj.test.CheckResult
import fj.test.Property
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class PropertyExtension {

	static CheckResult checkOkWithSummary(Property p) {
		checkBooleanWithNullableSummary(p, true)
	}

	static CheckResult checkOkWithNullableSummary(Property p) {
		checkBooleanWithNullableSummary(p, true)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static CheckResult checkBooleanWithNullableSummary(Property p, boolean b) {
		def cr = p.check()
		CheckResult.summaryNullable().println(cr)
		assertTrue(cr.isOk() == b)
		cr
	}


}
