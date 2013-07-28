package com.github.mperry.fg.demo

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 15/07/13
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */

import static com.github.mperry.fg.Comprehension.foreach
import static fj.data.Array.array
import static fj.Show.arrayShow
import static fj.Show.intShow
import fj.F
import fj.F2

import static com.github.mperry.fg.Comprehension.foreach
class Demo4 {

	@Test
	void test1() {

		assert [1, 4, 9, 16, 25] == com.github.mperry.fg.Comprehension.foreach {
			num << 1.to(5)
			yield { num * num }
		}.toJList()

// no 'as' needed for map because of recent SAM closure coercion

		fj.Show.arrayShow(fj.Show.intShow).println(fj.data.Array.array(1, 2, 3).map{int i -> i + 42}) // => {43,44,45}

// but can't yet get rid of 'as' using SAM coercion here since foldLeft
// is overridden even though it has only one type of each arity

		def s = 1.to(5)
		def f2 = { Integer x -> { Integer y -> x + y } as F } as F
		def a = s.foldLeft(f2, 0)
		def b = s.foldLeft({ Integer x, Integer y -> x + y } as F2, 0)
		def c = s.fold(0, { Integer x, Integer y -> x + y })
		assert [a, b, c] == [15] * 3



	}

}
