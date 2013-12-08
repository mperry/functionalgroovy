package com.github.mperry.fg.test

import com.gihub.mperry.ShowTester
import fj.F
import fj.Show
import fj.test.Arg
import fj.test.CheckResult
import groovy.transform.TypeChecked

import static com.github.mperry.fg.test.ArgStaticExtension.argShowNullable
import static fj.Show.showS

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class CheckResultStaticExtension {

	static Show<CheckResult> summaryNullable(CheckResult cr) {
		CheckResult.summary(ShowTester.argShowNullable())
//		CheckResult.summary(Arg.argShowNullable())
	}

}
