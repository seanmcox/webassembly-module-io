package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-elem
 * 
 * @author seanmcox
 *
 */
public class ElementSection extends Section {
	private Vector<Element> elems;

	/**
	 * @param elems
	 */
	public ElementSection(Vector<Element> elems) {
		this.elems = elems;
	}

	/**
	 * @return the elems
	 */
	public Vector<Element> getElems() {
		return elems;
	}
}
