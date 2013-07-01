package com.github.mperry.fg.euler

import fj.data.Stream
import org.junit.Test

/*
 * The prime factors of 13195 are 5, 7, 13 and 29.
 *
 * What is the largest prime factor of the number 600851475143 ?
 *
 */
class P03 extends GroovyTestCase {

	Stream<Integer> factors(BigInteger n) {
		factorsC(n, 1)
	}


	Stream<Integer> factors(BigInteger numerator, BigInteger denominator) {
		numerator == 1 ? Stream.nil() :
		(numerator % denominator != 0) ?
			factors(numerator, denominator + 1) :
			factors((numerator / denominator).toBigInteger(), denominator + 1).cons(denominator)
	}

	Closure<Stream<BigInteger>> factorsC = { BigInteger numerator, BigInteger denominator ->
		if (numerator == 1) {
			Stream.<BigInteger>nil()
		} else if (numerator % denominator != 0) {
			factorsC.trampoline(numerator, denominator + 1)
		} else {
			def s = factorsC.trampoline().call((numerator / denominator).toBigInteger(), 2)
			s.cons(denominator)
		}
	}.trampoline()

	@Test
	void test() {
//		println factors(12).toList()
		println factors(13195).toList()
		def list = factors(600851475143).toList()
		println list
		assertTrue(6857 == list.last())
	}

}
