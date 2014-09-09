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

//    IO<String> stdReadLine = { -> System.in.newReader().readLine() } as IO<String>

//    IO<Unit> stdPrintLine(String line) {
//        { -> System.out.println(line) } as IO<Unit>
//    }
//
//    IO<String> read() {
//        stdReadLine
//    }

    IO<List<File>> listFiles(File f) {
        { ->
            def files = new ArrayList<File>()
            files.addAll(f.listFiles())
            files
        } as IO<List<File>>
    }

    IO<Long> size(File f) {
        { -> f.length() } as IO
    }

    IO<String> info(File f) {
        { -> "${f.name}:${f.length()}" } as IO
    }

    void run(IO<List<String>> io) {
        def io2 = monad().map(io, { List<String> list -> list.join("\n") })
        println(io2.run())
    }

    @Test
    void sequence() {
        def io1 = monad().flatMap(listFiles(new File(".")), { List<File> list ->
            monad().sequence(list.map({ File f -> info(f) })) as IO<List<String>>
        })
        run(io1)
//        def io2 = map(io1, { List<String> list -> list.join("\n") })
//        def list = io2.run()
//        println(list)
        // io.run returns a list with two string values from standard io
    }

    @Test
    void traverse() {
        def io1 = monad().flatMap(listFiles(new File(".")), { List<File> list ->
            monad().traverse(list, { File f -> info(f) }) as IO<List<String>>
        })

        run(io1)
//        def io2 = map(io1, { List<String> list -> list.join("\n") })
//        println(io2.run())
    }



}
