package com.github.mperry.fg.test.dbc

import com.github.mperry.fg.test.PropertyConfig
import fj.F
import fj.F2
import fj.P1
import fj.test.Arbitrary
import fj.test.Gen
import fj.test.Property
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.test.PropertyConfig.getDEFAULT_MAP
import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.test.Arbitrary.arbitrary

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/08/13
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class StackTest {

	ExceptionFreeStack<Integer> empty() {
		new ExceptionFreeStack<Integer>()
	}

	Gen<ExceptionFreeStack<Integer>> genEmpty() {
		Gen.value(empty())
	}

	Gen<P1<ExceptionFreeStack<Integer>>> genLazyEmpty() {
		Gen.value({empty()} as P1<ExceptionFreeStack<Integer>>)
	}

	Gen<ExceptionFreeStack<Integer>> genNonEmpty() {
		Arbitrary.arbInteger.gen.bind(genStack(), { Integer element ->
			{ ExceptionFreeStack<Integer> s ->
				def newStack = new ExceptionFreeStack(s)
				newStack.push(element)
				newStack
			} as F
		} as F)
	}

	Gen<P1<ExceptionFreeStack<Integer>>> genLazyNonEmpty() {
		Arbitrary.arbInteger.gen.bind(genLazyStack(), { Integer element ->
			{ P1<ExceptionFreeStack<Integer>> p ->
				p.map({ ExceptionFreeStack<Integer> s ->
					def newStack = new ExceptionFreeStack<Integer>(s)
					newStack.push(element)
					newStack
				} as F)
			} as F
		} as F)
	}

	Arbitrary<P1<ExceptionFreeStack<Integer>>> arbStack() {
		arbitrary(genStack())
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	Gen<P1<ExceptionFreeStack<Integer>>> genStack() {
		Gen.elements({genEmpty()} as P1, {genNonEmpty()} as P1)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	Gen<P1<ExceptionFreeStack<Integer>>> genLazyStack() {
		Gen.oneOf([genLazyEmpty(), genLazyNonEmpty()])
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	Arbitrary<ExceptionFreeStack<Integer>> lazyArbStack() {
		arbitrary(genLazyStack().map({ it._1() } as F))
	}

//	@Test
	void testPush() {
//		def lazyArbStack = arbitrary(arbStack().gen.map({ P1 p -> p._1() } as F))
		showAll new PropertyConfig(
			map: DEFAULT_MAP + [(ExceptionFreeStack.class): lazyArbStack()],
			function: { ExceptionFreeStack<Integer> s, Integer i ->
				println "pushing $i onto ${s.toString()}"
				s.push(i)
				s.top() == i
			}
		)
	}

	@Test
	void test2() {
		def p = Property.property(Arbitrary.arbitrary(Gen.listOf(Arbitrary.arbInteger.gen)), Arbitrary.arbInteger,
				{ fj.data.List<Integer> list, Integer i ->
					println list
//				Bool.bool(true)
					Property.prop(true)
				} as F2
		)
		p.checkOkWithSummary()
	}

	@Test
	void intStack() {

		def p = Property.property(Arbitrary.arbitrary(Gen.listOf(Arbitrary.arbInteger.gen)), Arbitrary.arbInteger,
				{ fj.data.List<Integer> list, Integer i ->

			try {
				def uList = list.toList()
				def s = new ExceptionFreeStack<Integer>(uList)
				def s2 = new ExceptionFreeStack<Integer>(s)
				s.push(i)
				s.pop()
				def b = s.elements == s2.elements
				Property.prop(b)
			} catch (Exception e) {
				int z = 0
			} catch (Throwable t) {
				int x = 0
			}


		} as F2)
		p.checkOkWithSummary()
	}


}
