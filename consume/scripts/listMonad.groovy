
//@GrabResolver('https://oss.sonatype.org/content/groups/public')
@GrabResolver('https://oss.sonatype.org/content/repositories/snapshots/')
@Grab('com.github.mperry:functionalgroovy-main:0.5-SNAPSHOT')
import fj.F
import fj.F2
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.Unit.unit
import static junit.framework.Assert.assertTrue

@TypeChecked
class ListMonadExtensionsTest {

    @Test
    void join() {
        assertTrue(List.join([[1, 2], [3, 4], []]) == [1, 2, 3, 4])
    }

    @Test
    void map() {
        assertTrue([1, 2, 3].map({ Integer i -> i * 2} as F) == [2, 4, 6])
    }

    @Test
    void map2() {
        def f = { Integer i, Integer j -> i * j } as F2
        def list = [1, 2].map2([3, 4], f)
        assertTrue(list == [3, 4, 6, 8])
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void to() {
        def s = "a"
        def list = [1, 2].to(s)
        assertTrue([s, s] == list)
    }

    @Test
    void skip() {
        assertTrue([1, 2].skip() == [unit(), unit()])
    }

    @Test
    void foldM() {
        def list = List.foldM(1.to(3), 0, { Integer acc, Integer i -> [acc + i] } as F2)
        assertTrue(list == [6])
    }

    @Test
    void foldM_() {
        def list = List.foldM_(1.to(3), 0, { Integer acc, Integer i -> [acc + i] } as F2)
        assertTrue(list == [unit()])
    }

    @Test
    void sequence() {
        def list = List.sequence([[1, 2], [3, 4, 5]])
        def haskell = [[1,3],[1,4],[1,5],[2,3],[2,4],[2,5]]
        assertTrue(list == haskell)
    }

    @Test
    void traverse() {
        def list = [1, 2, 3].traverse({ Integer i -> 1.to(i).toJList()} as F)
        def haskell = [[1,1,1],[1,1,2],[1,1,3],[1,2,1],[1,2,2],[1,2,3]]
        assertTrue(list == haskell)
    }

    @Test
    void replicateM() {
        def list = [1, 2].replicateM(3)
        def haskell = [[1,1,1],[1,1,2],[1,2,1],[1,2,2],[2,1,1],[2,1,2],[2,2,1],[2,2,2]]
        assertTrue(list == haskell)
    }

    @Test
    void compose() {
        def f = { String s -> [s, s] } as F
        def g = { Integer i ->
            1.to(i * 2).toJavaList().map({ Integer j ->
                j.toString()
            } as F)
        } as F
        def func = List.compose(f, g)
        def list = func.f(3)
        def expected = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6].collect { it.toString() }
        assertTrue(list == expected)
    }

    @Test
    void filterM() {
        def f = { Integer i ->
            [i > 0]
        } as F
        def actual = List.filterM([2, 1, 0, -1], f)
        assertTrue(actual == [[2, 1]])
    }

    @Test
    void when() {
        // TODO: not sure if this is applicable
    }

    @Test
    void unless() {
        // TODO: not sure if this is applicable
    }

    @Test
    void liftM() {
        def source = [1, 2, 3]
        def actual = source.liftM { Integer i ->
            i * 2
        }
        def expected = source.map { Integer it -> it * 2 }
        assertTrue(expected == actual)
    }

    @Test
    void liftM2() {
        def actual = [0, 1].liftM2([0, 2], {Integer i, Integer j -> i + j})
        assertTrue(actual == [0, 2, 1, 3])
    }

    @Test
    void liftM3() {
        def actual = [0, 1].liftM3([0, 2], [0, 4], {Integer i, Integer j, Integer k -> i + j + k })
        def expected = [0, 4, 2, 6, 1, 5, 3, 7]
        assertTrue(actual == expected)
    }

    @Test
    void ap() {
        // Haskell:
        // Prelude Control.Monad> ap (map (\x -> (\i -> i * (x + 2))) [3..5]) [1..4]
        def fs = 3.to(5).map { Integer i ->
            def f = { Integer j -> j * (i + 2)}
            f as F
        }.toJavaList()
        def actual = 1.to(4).toJavaList().ap(fs)
        def expected = [5,10,15,20,6,12,18,24,7,14,21,28]
        assertTrue(actual == expected)
    }

    @Test
    void apply() {
        // same implementation as ap
    }

}
