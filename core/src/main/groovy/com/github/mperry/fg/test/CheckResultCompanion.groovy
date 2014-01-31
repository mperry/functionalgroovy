package com.github.mperry.fg.test

import com.github.mperry.ShowWorkaroundJava
import fj.Show
import fj.test.CheckResult
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 31/01/14.
 */
@TypeChecked
class CheckResultCompanion {

    static Show<CheckResult> summaryNullable() {
        CheckResult.summary(ShowWorkaroundJava.argShowNullable())
    }

}
