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

    static IOMonad monad = new IOMonad()

    static IO<List<File>> listFiles(File f) {
        { ->
            def files = new ArrayList<File>()
            files.addAll(f.listFiles())
            files
        } as IO<List<File>>
    }

    static IO<List<File>> listFiles() {
        listFiles(new File("."))
    }

    static IO<Long> size(File f) {
        { -> f.length() } as IO
    }

    static IO<String> info(File f) {
        { -> "${f.name}:${f.length()}" } as IO
    }

    @Test
    void sequence() {
        def io = monad.flatMap(listFiles(), { List<File> list ->
            monad.sequence(list.map{ File f -> info(f) }) as IO<List<String>>
        })
        println(io.run().join("\n"))
    }

    @Test
    void traverse() {
        def io = monad.flatMap(listFiles(), { List<File> list ->
            monad.traverse(list, { File f -> info(f) }) as IO<List<String>>
        })
        println(io.run().join("\n"))
    }

}
