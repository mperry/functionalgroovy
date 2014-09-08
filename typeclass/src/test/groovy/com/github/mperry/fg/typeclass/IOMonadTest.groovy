package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.IOMonad
import fj.data.IO
import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by MarkPerry on 7/09/2014.
 */
@TypeChecked
class IOMonadTest {

    IOMonad monad() {
        new IOMonad()
    }

    IO<String> stdReadLine = { -> System.in.newReader().readLine() } as IO<String>

    IO<String> read() {
        stdReadLine
    }

    @Test
    void sequence() {
        def io = monad().sequence([read(), read()])
        // io.run returns a list with two string values from standard io
    }

    @Test
    void traverse() {
        def f = { Integer i -> monad().map(read(), { String s -> Integer.parseInt(s) == i * i })}
        monad().traverse([1, 3, 5], f)
    }

}
