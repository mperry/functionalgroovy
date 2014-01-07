package com.github.mperry.fg.test

import fj.F
import fj.Show
import fj.Unit
import fj.data.Stream
import fj.test.Arg
import fj.test.CheckResult
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 12:49 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class CheckResultExtension {

	static Unit printlnSummary(CheckResult cr) {
		CheckResult.summary.println(cr)
	}

    @TypeChecked(TypeCheckingMode.SKIP)
	static Unit printlnSummaryNullable(CheckResult cr) {
//        CheckResult.summaryNullable().println(cr)
		CheckResultStaticExtension.summaryNullable().println(cr)
//		CheckResultExtension.nullableSummary()
	}

	static boolean isOk(CheckResult cr) {
		cr.isPassed() || cr.isProven()
	}



}
