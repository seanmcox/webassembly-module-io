/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @author seanmcox
 *
 */
public class FunctionSection extends Section {
	private Vector<TypeIndex> typeidxes;

	public FunctionSection(Vector<TypeIndex> typeidxes) {
		this.typeidxes = typeidxes;
	}

	/**
	 * @return the typeidxes
	 */
	public Vector<TypeIndex> getTypeidxes() {
		return typeidxes;
	}
}
