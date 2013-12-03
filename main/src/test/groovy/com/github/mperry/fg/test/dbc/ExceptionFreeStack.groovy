package com.github.mperry.fg.test.dbc

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 2/07/13
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */

import org.gcontracts.annotations.*

@Invariant({ elements != null && size() >= 0 })
class ExceptionFreeStack<T> {

	List<T> elements

	@Ensures({ isEmpty() })
	ExceptionFreeStack() {
		elements = []
	}

	@Requires({ list != null })
	@Ensures({ size() == list.size() })
	ExceptionFreeStack(List<T> list)  {
		elements = new ArrayList<T>(list)
	}

	@Requires({ stack != null })
	@Ensures({ size() == stack.size() })
	ExceptionFreeStack(ExceptionFreeStack<T> stack) {
		elements = new ArrayList<T>(stack.elements)
	}

	boolean isEmpty()  {
		elements.isEmpty()
	}

	@Requires({ !isEmpty() })
	T top()  {
		elements.last()
	}

	@Ensures({ result -> result >= 0 })
	int size() {
		elements.size()
	}

	@Ensures({ result -> result.implies(size() > 0) })
	boolean has(T item)  {
		elements.contains(item)
	}

	@Ensures({
//        TODO: appears to be a bug in GContracts where old is a LinkedHashMap which is always empty
//        old -> top() == item && old.elements.size() + 1 == size()
//        old -> compare(old)
        top() == item
    })
	void push(T item)  {
		elements.add(item)
	}

	@Requires({ !isEmpty() })
	T pop()  {
		elements.pop()
	}

	String toString() {
		elements.toString()
	}

}
