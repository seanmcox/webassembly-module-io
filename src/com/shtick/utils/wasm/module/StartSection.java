/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class StartSection extends Section {
	private Start start;

	/**
	 * @param start
	 */
	public StartSection(Start start) {
		this.start = start;
	}

	/**
	 * @return the start
	 */
	public Start getStart() {
		return start;
	}
}
