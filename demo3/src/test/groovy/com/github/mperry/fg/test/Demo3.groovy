package com.github.mperry.fg.test

import org.junit.Test

@GrabResolver('https://raw.github.com/mperry/functionalgroovy/master/releases/')
@Grab('org.functionalgroovy:functionalgroovy-main:0.1.1-SNAPSHOT')
// next two are temporary until maven artifact reference is corrected
@Grab('org.functionalgroovy:functionalgroovy-core:0.1.1-SNAPSHOT')
//@GrabExclude('functionalgroovy:core')
import static com.github.mperry.fg.Comprehension.foreach
class Demo3 {

	@Test
	void test1() {
		def result = foreach {
			num { 1.to(2) }
			yield { num + 1 }
		}
		assert result.toJList() == [2, 3]

	}
}
