package com.github.mperry.fg

import fj.P1
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test
import fj.data.Stream

import static org.junit.Assert.assertTrue
import fj.F
import fj.F2
//import com.github.mperry.fg.StreamExtension

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 20/02/13
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class StreamTest {

	@Test
	void simple() {
		def s = Stream.range(1)
		def s2 = s.filter { Integer it -> it % 2 == 0}.take(4)
		assertTrue(s2.toJList() == [2, 4, 6, 8])
	}

	@Test
	void combos2() {
		def a = 1.to(2).combos(3.to(4))
		def expected = [[1, 3], [1, 4], [2, 3], [2, 4]]
		assertTrue(a.toJList() == expected)
	}

	@Test
	void combos3() {
		def b = 1.to(2).combos(3.to(4)).combos(5.to(6))
		def expected = [[1, 3, 5], [1, 3, 6], [1, 4, 5], [1, 4, 6],[2, 3, 5], [ 2, 3, 6], [2, 4, 5], [2, 4, 6]]
		assertTrue(b.toJList() == expected)
	}

	@Test
	void combos4() {
		def b = 1.to(2).combos(3.to(4)).combos(5.to(6)).combos(7.to(8))
		def expected = [[1, 3, 5, 7], [1, 3, 5, 8], [1, 3, 6, 7], [1, 3, 6, 8], [1, 4, 5, 7], [1, 4, 5, 8],
				[1, 4, 6, 7], [1, 4, 6, 8], [2, 3, 5, 7], [2, 3, 5, 8], [2, 3, 6, 7], [2, 3, 6, 8],
				[2, 4, 5, 7], [2, 4, 5, 8], [2, 4, 6, 7], [2, 4, 6, 8]]
		assertTrue(b.toJList() == expected)
	}

	@Test
	void mapWithSubstreams() {
		def b = 1.to(2).map{ Integer it -> it.to(4)}
		assertTrue(b.toJList() == [[1, 2, 3, 4], [2, 3, 4]])
	}

	@Test
	void simpleToJList() {
		def a = 1.to(3)
		assertTrue(a.toJList() == [1, 2, 3])
	}

	@Test
    @TypeChecked(TypeCheckingMode.SKIP)
	void fold() {
		def s = 1.to(5)
		def f2 = { Integer x -> { Integer y -> x + y } as F } as F
		def a = s.foldLeft(f2, 0)
		def b = s.foldLeft({Integer x, Integer y -> x + y} as F2, 0)
		def c = s.foldLeft({Integer x, Integer y -> x + y} as F2, 0)
		assertTrue(a == 15)
		assertTrue(b == 15)
		assertTrue(c == 15)
	}

//    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void streamFlatMapOverflows() {
        def max = 1000000
        def p = { ->
            def s = 1.to(max).map { Integer i ->
                [i, i + 1]
            }.flatMap { List<Integer> list ->
                Stream.stream(list[0], list[1])
            }
            def list = s.toJavaList()
            println list
        } as P1
        assertTrue(p.throwsError(StackOverflowError.class))
    }

}
