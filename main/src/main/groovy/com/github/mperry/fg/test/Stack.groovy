package com.github.mperry.fg.test

import groovy.transform.TypeChecked
import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Invariant
import org.gcontracts.annotations.Requires

/**
 * Created with IntelliJ IDEA.
 * User: PerryMa
 * Date: 2/07/13
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
@Invariant({ elements != null })
class Stack<T> {

	List<T> elements

	@Ensures({ isEmpty() })
	Stack()  {
		elements = []
	}

	@Requires({ preElements?.size() > 0 })
	@Ensures({ !isEmpty() })
	Stack(List<T> preElements)  {
		elements = preElements
	}

	boolean isEmpty()  {
		elements.isEmpty()
	}

	@Requires({ !isEmpty() })
	T last_item()  {
		elements.get(count() - 1)
	}

	int count() {
		elements.size()
	}

	@Ensures({ result == true ? count() > 0 : count() >= 0  })
	boolean has(T item)  {
		return elements.contains(item)
	}

	@Ensures({ last_item() == item })
	void push(T item)  {
		elements.add(item)
	}

	@Requires({ !isEmpty() })
	@Ensures({ last_item() == item })
	void replace(T item)  {
		pop()
		push(item)
	}

	@Requires({ !isEmpty() })
	@Ensures({ result != null })
	T pop()  {
		return elements.remove(count() - 1)
	}

	String toString() {
		elements.toString()
	}

}

//def stack = new Stack<Integer>()