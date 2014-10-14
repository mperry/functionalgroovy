@GrabResolver(name='custom', root='https://oss.sonatype.org/content/groups/public', m2Compatible=true)
//@Grab('org.functionaljava:functionaljava:4.2-SNAPSHOT')
@Grab('com.github.mperry:functionalgroovy-core:0.6-SNAPSHOT')

import fj.*
import fj.data.*
import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by mperry on 4/08/2014.
 */
@TypeChecked
class ExceptionHandling {

    Validation<? extends IOException, String> contents(String s) {
        ({ -> (new URL(s)).text } as P1).validate()
    }

    Validation<? extends IOException, String> contents2(String s) {
        P.lazy({ -> (new URL(s)).text } as P1).validate()
    }

    @Test
    void errorTest() {
        def actual = ["malformed", "http://www.this-page-intentionally-left-blank.org/"].collect { String s ->
            contents2(s).isSuccess()
        }
        assert actual == [false, true]
    }

}





