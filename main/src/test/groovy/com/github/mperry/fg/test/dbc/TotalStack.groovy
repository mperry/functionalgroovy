package com.github.mperry.fg.test.dbc

import fj.data.Option

import static fj.data.Option.none
import static fj.data.Option.some

/**
 * Simple total Stack (returns no exceptions)
 */
class TotalStack<T> {

	List<T> elements

	TotalStack() {
		elements = []
	}

	boolean isEmpty()  {
		elements.isEmpty()
	}

	Option<T> top()  {
		isEmpty() ? none() : some(elements.last())
	}

	int size() {
		elements.size()
	}

	void push(T item)  {
		elements.add(item)
	}

	Option<T> pop()  {
		isEmpty() ? none() : some(elements.pop())
	}

	String toString() {
		elements.toString()
	}

}
