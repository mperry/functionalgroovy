package com.github.mperry.fg.demo

import org.junit.Test
import fj.data.Option
//import com.github.mperry.fg.Option

@GrabResolver('https://oss.sonatype.org/content/groups/public')
//@GrabResolver('https://raw.github.com/mperry/functionalgroovy/master/releases/')
//@Grab('org.functionalgroovy:functionalgroovy-main:0.1.1-SNAPSHOT')
@Grab('com.github.mperry:functionalgroovy-core:0.2-SNAPSHOT')
// next two are temporary until maven artifact reference is corrected
//@Grab('org.functionalgroovy:functionalgroovy-core:0.1.1-SNAPSHOT')
@GrabExclude('functionalgroovy:core')
class Demo extends GroovyTestCase {

	@Test
	void test1() {
		def o = Option.some(1).map {
			it + 2
		}

		assertTrue(o.some() == 3)
	}

}
