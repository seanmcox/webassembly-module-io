/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @see https://webassembly.github.io/spec/core/binary/modules.html#data-section
 * @author seanmcox
 *
 */
public class DataSection extends Section{
	private Vector<Data> data;

	/**
	 * @param data
	 */
	public DataSection(Vector<Data> data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public Vector<Data> getData() {
		return data;
	}
}
