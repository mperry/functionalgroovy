package com.github.mperry.fg.test

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

	@Ensures({ is_empty() })
	def Stack()  {
		elements = []
	}

	@Requires({ preElements?.size() > 0 })
	@Ensures({ !is_empty() })
	def Stack(List<T> preElements)  {
		elements = preElements
	}

	boolean is_empty()  {
		return elements.isEmpty()
	}

	@Requires({ !is_empty() })
	T last_item()  {
		return elements.get(count() - 1)
	}

	def count() {
		return elements.size()
	}

	@Ensures({ result ? count() > 0 : count() >= 0  })
	boolean has(T item)  {
		return elements.contains(item)
	}

	@Ensures({ last_item() == item })
	def push(T item)  {
		return elements.add(item)
	}

	@Requires({ !is_empty() })
	@Ensures({ last_item() == item })
	def replace(T item)  {
		remove()
		return elements.add(item)
	}

	@Requires({ !is_empty() })
	@Ensures({ result != null })
	T remove()  {
		return elements.remove(count() - 1)
	}

	String toString() { elements.toString() }
}

//def stack = new Stack<Integer>()