package com.github.mperry.fg

import fj.F
import fj.Function
import fj.data.Stream
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by MarkPerry on 2/02/14.
 */
@TypeChecked
class SqlExtension {

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A> Stream<A> streamRowResult(ResultSet rs, F<GroovyRowResult, A> f) {
        stream(rs).map { ResultSet it ->
            it.toRowResult()
        }.map(f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A> Stream<A> streamResultSet(ResultSet rs, F<ResultSet, A> f) {
        stream(rs).map(f)
    }

    static <A, B> B fold(Sql sql, String query, F<Stream<ResultSet>, B> g) {
        fold(sql, query, Function.identity(), g)
    }

    static <A, B> B fold(Sql sql, String query, Closure<B> f) {
        fold(sql, query, f as F)
    }

    static <A, B> B fold(Sql sql, String query, F<ResultSet, A> f, F<Stream<A>, B> g) {
        def c
        def s
        try {
            c = sql.getConnection()
            s = c.createStatement()
            def rs = s.executeQuery(query)
            def b = g.f(streamResultSet(rs, f))
            b
        } catch (SQLException e) {
            e.printStackTrace()
        } finally {
            // external function's responsibility to close the Sql resource
//            sql.close()
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static F<ResultSet, GroovyRowResult> rowResult() {
        return { ResultSet rs -> rs.toRowResult() } as F
    }

    static <A, B> B foldRowResult(Sql sql, String query, F<Stream<GroovyRowResult>, B> g) {
        foldRowResult(sql, query, Function.identity(), g)
    }

    static <A, B> B foldRowResult(Sql sql, String query, Closure<B> f) {
        foldRowResult(sql, query, f as F)
    }

    static <A, B> B foldRowResult(Sql sql, String query, F<GroovyRowResult, A> f, F<Stream<A>, B> g) {
        fold(sql, query, f.o(rowResult()), g)
    }

    static Stream<ResultSet> stream(ResultSet rs) {
        SqlExtensionJava.toStream(rs)
    }

    static <A, B> Stream<ResultSet> stream(Sql sql, String query) {
        try {
            def c = sql.getConnection()
            def s = c.createStatement()
            def rs = s.executeQuery(query)
            stream(rs)
        } catch (SQLException e) {
            throw e
        }
    }

}
