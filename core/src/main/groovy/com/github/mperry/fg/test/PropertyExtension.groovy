package com.github.mperry.fg.test

import fj.Unit
import fj.test.Property

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
class PropertyExtension {

	static Unit checkOkWithSummary(Property p) {
		def cr = p.check()
		cr.printlnSummary()
		assertTrue(cr.isOk())
		Unit.unit()
	}

	static Unit checkOkWithNullableSummary(Property p) {
		checkBooleanWithNullableSummary(p, true)
//		def cr = p.check()
//		cr.printlnSummaryNullable()
//		assertTrue(cr.isOk())
//		Unit.unit()
	}

	static Unit checkBooleanWithNullableSummary(Property p, boolean b) {
		def cr = p.check()
		cr.printlnSummaryNullable()
		assertTrue(cr.isOk() == b)
		Unit.unit()
	}


}
