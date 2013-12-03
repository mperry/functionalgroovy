package com.github.mperry.fg.test.dbc

import com.github.mperry.fg.test.PropertyConfig
import fj.F
import fj.test.Arbitrary
import fj.test.Gen
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.gcontracts.AssertionViolation
import org.gcontracts.ClassInvariantViolation
import org.gcontracts.PostconditionViolation
import org.gcontracts.PreconditionViolation
import org.gcontracts.annotations.meta.Precondition
import org.junit.Test

import static com.github.mperry.fg.test.PropertyConfig.getDEFAULT_MAP
import static com.github.mperry.fg.test.PropertyConfig.validator
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

    Arbitrary<ExceptionFreeStack<Integer>> arbStack() {
        arbitrary(genStackRecursive())
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void testPush() {
        [genStackRecursive(), genStackImperative()].collect { g ->
            showAll new PropertyConfig(
                map: DEFAULT_MAP + [(ExceptionFreeStack.class): arbitrary(g)],
                function: { ExceptionFreeStack<Integer> s, Integer i ->
//                    println "pushing $i onto ${s.toString()}"
                    def newStack = new ExceptionFreeStack<Integer>(s)
                    newStack.push(i)
                    def b = newStack.top() == i
                    b
                },
                validator: validator(contractsOkFunc())
            )
        }
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void testPop() {
        showAll new PropertyConfig(
            truth: true,
            map: DEFAULT_MAP + [(ExceptionFreeStack.class): arbStack()],
            function: { ExceptionFreeStack<Integer> s ->
//                    println "pushing $i onto ${s.toString()}"
                def val = s.pop()
                true
            },
            validator: validator(contractsOkFunc())
        )
    }

    F<Throwable, Boolean> contractsOkFunc() {
        { Throwable t ->
            contractsOk(t)
        } as F
    }

    Boolean contractViolation(Throwable t) {
        hasType(t, AssertionViolation.class)
    }

    Boolean contractsOk(Throwable t) {
        def i = invariantOk(t)
        def pre = preOk(t)
        def post = postOk(t)
        i && (pre.implies(post))
    }

    Boolean preOk(Throwable t) {
        !hasType(t, PreconditionViolation.class)
    }

    Boolean postOk(Throwable t) {
        !hasType(t, PostconditionViolation.class)
    }

    Boolean invariantOk(Throwable t) {
        !hasType(t, ClassInvariantViolation.class)
    }

    Boolean hasType(Object o, Class c) {
        c.isAssignableFrom(o.getClass())
    }

}
