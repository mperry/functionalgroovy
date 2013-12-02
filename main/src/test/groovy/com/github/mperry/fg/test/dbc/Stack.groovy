package com.github.mperry.fg.test.dbc

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 2/07/13
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */

import org.gcontracts.annotations.*

@Invariant({ elements != null })
class Stack<T> {

	List<T> elements

	@Ensures({ isEmpty() })
	Stack() {
		elements = []
	}

	@Requires({ list != null })
//	@Ensures({ !isEmpty() })
	Stack(List<T> list)  {
		elements = new ArrayList<T>(list)
	}

	Stack(Stack<T> stack) {
		elements = new ArrayList<T>(stack.elements)
	}

	boolean isEmpty()  {
		elements.isEmpty()
	}

	@Requires({ !isEmpty() })
	T top()  {
//		elements.get(count() - 1)
		elements.last()
	}

	@Ensures({ result >= 0 })
	int count() {
		elements.size()
	}

	@Ensures({ result ? count() > 0 : count() >= 0  })
	boolean has(T item)  {
		elements.contains(item)
	}

	@Ensures({ top() == item })
	void push(T item)  {
		elements.add(item)
	}

	@Requires({ !isEmpty() })
	void pop()  {
		elements.remove(count() - 1)
	}

	String toString() {
		elements.toString()
	}


}
