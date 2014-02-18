package com.github.mperry.fg.state

import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 17/02/14.
 */
@TypeChecked
@Canonical
class Machine {

    Boolean locked
    Integer items
    Integer coins

}
