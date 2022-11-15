/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @author seanmcox
 *
 */
public class CodeSection extends Section {
	private Vector<Code> codes;

	/**
	 * @param codes
	 */
	public CodeSection(Vector<Code> codes) {
		this.codes = codes;
	}

	/**
	 * @return the codes
	 */
	public Vector<Code> getCodes() {
		return codes;
	}
}
