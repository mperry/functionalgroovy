package com.github.mperry.fg

import fj.F
import fj.P
import fj.Unit
import fj.data.Stream
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

import java.sql.ResultSet

import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 2/02/14.
 */
@TypeChecked
class SqlTest {

    static Sql sql
//    static Integer big = 1000002
    static Integer big = 100002
//    static Integer big = 500001
    static Integer small = 12
    static Integer collateSize = 3
    static Boolean databaseSetup = false

    @TypeChecked(TypeCheckingMode.SKIP)
    @Before
    void setup() {
//        resourceSetup()
        lazySetup()
    }

    @AfterClass
    static void close() {
        println "Closing sql..."
        sql.close()
    }

    static void resourceSetup() {
        time("setup") {
            sql = Sql.newInstance('jdbc:h2:mem:', 'org.h2.Driver')
            sql.execute 'CREATE TABLE big_table (col_a INTEGER, col_b INTEGER, col_c INTEGER)'
            sql.execute 'CREATE TABLE small_table (col_a INTEGER, col_b INTEGER, col_c INTEGER)'

            (1..big).collate(collateSize).each { List<Integer> list ->
                sql.execute("INSERT INTO big_table (col_a, col_b, col_c) VALUES (?, ?, ?)", list)
            }
            (1..small).collate(collateSize).each { List<Integer> list ->
                sql.execute("INSERT INTO small_table (col_a, col_b, col_c) VALUES (?, ?, ?)", list)
            }
        }
    }

    static void lazySetup () {
        if (!databaseSetup) {
            databaseSetup = true
            resourceSetup()
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldRowResultBig1() {
        time("foldRowResultBig1") {
            def list = sql.foldRowResult("SELECT * FROM big_table") { Stream<GroovyRowResult > s->
                s.map { GroovyRowResult rr ->
                    [rr.col_a + rr.col_b, rr.col_c * 2]
                }.toJavaList()
            }
//            println(list.size())
            assertTrue(list.size() == big / collateSize)
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldResultSetBig1() {
        time("foldResultSetBig1") {
            def list = sql.fold("SELECT * FROM big_table") { Stream<ResultSet> s ->

                s.map { ResultSet rs ->
                    [rs.getInt(1) + rs.getInt(2), rs.getInt(3) * 2]
                }.toJavaList()
            }
//            println(list.size())
            assertTrue(list.size() == big / collateSize)
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldResultSetBig2() {
        time("foldResultSetBig2") {
            def list = sql.fold("SELECT * FROM big_table") { Stream<ResultSet> s ->

                def f = { ResultSet rs ->
                    [rs.getInt(1) + rs.getInt(2), rs.getInt(3) * 2]
                } as F
                s.map(f).toJavaList()
            }
//            println(list.size())
            assertTrue(list.size() == big / collateSize)
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldRowResultSmall1() {
        time("foldRowResultSmall1") {
            def list = sql.foldRowResult("SELECT * FROM small_table") { Stream<GroovyRowResult> s ->
                s.map { GroovyRowResult rr ->
                    [rr.col_a + rr.col_b, rr.col_c * 2]
                }.toJavaList()
            }
//            println(list.size())
            assertTrue(list.size() == small / collateSize)
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldResultSetSmall1() {
        time("rows") {
            def list = sql.fold("SELECT * FROM small_table") { Stream<ResultSet> s ->
                s.flatMap { ResultSet rs ->
                    Stream.stream(rs.getInt(1) + rs.getInt(2), rs.getInt(3) * 2)
                }.toJavaList()
            }
//            println(list.size())
            assertTrue(list.size() == small * 2 / collateSize)
        }
    }

    static <T> T time(String desc, Closure<T> act) {
        def start = System.nanoTime()
        try {
            act()
        } finally {
            println "time [$desc]: ${(System.nanoTime() - start) / 1e6} ms"
        }
    }

}
