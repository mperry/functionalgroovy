
@GrabResolver('https://oss.sonatype.org/content/repositories/snapshots/')
@Grab('com.github.mperry:functionalgroovy-main:0.5-SNAPSHOT')

import groovy.transform.TypeChecked
import org.junit.Test

import static junit.framework.Assert.assertTrue

@TypeChecked
class StreamTest {

    @Test
    void explicitBox() {
        assertTrue(new Integer(1).to(3).toJavaList() == (1..3).toList())
    }

    @Test
    void implicitBox() {
        // fails with error:

        /*
        Caught: BUG! exception in phase 'instruction selection' in source unit 'D:\repositories\functionalgroovy\consume\scripts\stream.groovy' Declaring class for method call to 'fj.data.Stream to(java.lang.Integer, java.lang.Integer)' declared in java.lang.Integer was not matched with found receiver int. This should not have happened!

BUG! exception in phase 'instruction selection' in source unit 'D:\repositories\functionalgroovy\consume\scripts\stream.groovy' Declaring class for method call to 'fj.data.Stream to(java.lang.Integer, java.lang.Integer)' declared in java.lang.Integer was not matched with found receiver int. This should not have happened!

         */

        // comment out this test to demonstrate that the explicit boxing test
        // above works
        assertTrue(1.to(3).toJavaList() == (1..3).toList())
    }

}
