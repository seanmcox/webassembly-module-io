/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @author seanmcox
 *
 */
public class DataSection {
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
