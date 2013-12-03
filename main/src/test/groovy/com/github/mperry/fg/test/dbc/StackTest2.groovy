package com.github.mperry.fg.test.dbc

import com.github.mperry.fg.test.PropertyConfig
import fj.F
import fj.test.Arbitrary
import fj.test.Gen
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.test.PropertyConfig.getDEFAULT_MAP
import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.test.Arbitrary.arbitrary

/**
 * Created with IntelliJ IDEA.
 * User: mwp
 * Date: 3/12/13
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class StackTest2 {

    ExceptionFreeStack<Integer> empty() {
        new ExceptionFreeStack<Integer>()
    }

    Gen<Integer> genStackSize() {
        Gen.oneOf([Gen.value(0), Gen.value(1), Gen.choose(2, 10)].toFJList())
    }

    Gen<ExceptionFreeStack<Integer>> genStackImperative() {
        genStackSize().map({ Integer n ->
            def s = empty()
            def r = new Random()
//            println "Creating stack of size $n"
            for (int i = 0; i < n; i++) {
                def val = r.nextInt()
                s.push(val)
            }
            s
        } as F)
    }

    Gen<ExceptionFreeStack<Integer>> genEmpty() {
        Gen.value(empty())
    }

    Gen<ExceptionFreeStack<Integer>> genNonEmpty() {
        Arbitrary.arbInteger.gen.bind({Integer i ->
            def g = genStackRecursive().map({ ExceptionFreeStack s ->
                s.push(i)
                s
            } as F)
        } as F)
    }

    Gen<ExceptionFreeStack<Integer>> genStackRecursive() {
        Gen.oneOf([genEmpty(), genNonEmpty()].toFJList())
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void testPush() {
        [genStackRecursive(), genStackImperative()].collect { g ->
            showAll new PropertyConfig(
                map: DEFAULT_MAP + [(ExceptionFreeStack.class): arbitrary(g)],
                function: { ExceptionFreeStack<Integer> s, Integer i ->
//                    println "pushing $i onto ${s.toString()}"
                    s.push(i)
                    s.top() == i
                }
            )
        }
    }

}
