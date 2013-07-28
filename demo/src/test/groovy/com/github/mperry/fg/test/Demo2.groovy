package com.github.mperry.fg.demo

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 27/06/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
@GrabResolver('https://raw.github.com/mperry/functionalgroovy/master/releases/')
@Grab('org.functionalgroovy:functionalgroovy-main:0.1.1-SNAPSHOT')
// next two are temporary until maven artifact reference is corrected
@Grab('org.functionalgroovy:functionalgroovy-core:0.1.1-SNAPSHOT')
@GrabExclude('functionalgroovy:core')
class Demo2 extends GroovyTestCase {


	@Test
	void test1() {
		println("hello world")
	}

}
