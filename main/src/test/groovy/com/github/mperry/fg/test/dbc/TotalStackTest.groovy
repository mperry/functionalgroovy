package com.github.mperry.fg.test.dbc

import com.github.mperry.fg.test.Model
import fj.F
import fj.test.Arbitrary
import fj.test.Gen
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.test.Model.getDEFAULT_MAP
import static com.github.mperry.fg.test.Specification.spec
import static com.github.mperry.fg.test.Specification.specAssert
import static fj.test.Arbitrary.arbitrary

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 10/12/13
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
class TotalStackTest {

	TotalStack<Integer> empty() {
		new TotalStack<Integer>()
	}

	Gen<Integer> genStackSize() {
		Gen.oneOf([Gen.value(0), Gen.value(1), Gen.choose(2, 10)].toFJList())
	}

	Gen<TotalStack<Integer>> genStackLoop() {
		genStackSize().map({ Integer n ->
			def s = empty()
			def r = new Random()
			for (int i = 0; i < n; i++) {
				s.push(r.nextInt())
			}
			s
		} as F)
	}

	Gen<TotalStack<Integer>> genEmpty() {
		Gen.value(empty())
	}

	Gen<TotalStack<Integer>> genNonEmpty() {
		Arbitrary.arbInteger.gen.bind({Integer i ->
			genStackRecursive().map({ TotalStack s ->
				s.push(i)
				s
			} as F)
		} as F)
	}

	Gen<TotalStack<Integer>> genStackRecursive() {
		Gen.oneOf([genEmpty(), genNonEmpty()].toFJList())
	}

	Arbitrary<TotalStack<Integer>> arbStack() {
		arbitrary(genStackRecursive())
	}

	@Test
//	@TypeChecked(TypeCheckingMode.SKIP)
	void testPush() {
		[genStackRecursive(), genStackLoop()].each { g ->
			specAssert new Model(
					map: DEFAULT_MAP + [(TotalStack.class): arbitrary(g)],
					function: { TotalStack<Integer> s, Integer i ->
						s.push(i)
						def val = s.top()
						val.map { it == i }.orSome(false)
					}
			)
		}
	}

	@Test
	@TypeChecked(TypeCheckingMode.SKIP)
	void testTop() {
		def r = new Random()
		specAssert new Model(
				map: DEFAULT_MAP + [(TotalStack.class): arbStack()],
				function: { TotalStack<Integer> s ->
					def i = r.nextInt()
					s.push(i)
					s.top().map{ it == i}.orSome(false)
				}
		)
	}

}
