/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @author seanmcox
 *
 */
public class MemorySection extends Section {
	private Vector<Memory> mems;

	/**
	 * @param mems
	 */
	public MemorySection(Vector<Memory> mems) {
		this.mems = mems;
	}

	/**
	 * @return the mems
	 */
	public Vector<Memory> getMems() {
		return mems;
	}
}
