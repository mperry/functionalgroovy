package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.IOMonad
import fj.F
import fj.F2
import fj.function.Integers
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.Function.uncurryF2
import static fj.data.Option.none
import static fj.data.Option.some
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 15/07/2014.
 */
@TypeChecked
class IOMonadTest {


    IOMonad monad() {
        new IOMonad()
    }


}
