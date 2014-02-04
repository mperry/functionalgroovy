package com.github.mperry.fg;

import fj.F;
import fj.P1;
import fj.data.Stream;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by MarkPerry on 2/02/14.
 */
public class SqlExtensionJava {

    static <A> Stream<A> toStream(final ResultSet rs, final F<ResultSet, A> f)
            throws SQLException {
        return toStream(rs).map(f);
    }

    static <A> Stream<ResultSet> toStream(final ResultSet rs) throws SQLException {
        boolean b = rs.next();
        if (!b) {
            return Stream.nil();
        } else {
            return Stream.cons(rs, new P1<Stream<ResultSet>>() {
                @Override
                public Stream<ResultSet> _1() {
                    try {
                        return toStream(rs);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return Stream.nil();
                    }
                }
            });
        }
    }

}
