package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-global
 * 
 * @author seanmcox
 *
 */
public class GlobalSection extends Section {
	private Vector<Global> globals;

	/**
	 * @param globals
	 */
	public GlobalSection(Vector<Global> globals) {
		this.globals = globals;
	}

	/**
	 * @return the global
	 */
	public Vector<Global> getGlobals() {
		return globals;
	}
}
