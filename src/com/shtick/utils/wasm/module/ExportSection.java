/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-export
 * 
 * @author seanmcox
 *
 */
public class ExportSection extends Section {
	private Vector<Export> exports;

	/**
	 * @param exports
	 */
	public ExportSection(Vector<Export> exports) {
		this.exports = exports;
	}

	/**
	 * @return the exports
	 */
	public Vector<Export> getExports() {
		return exports;
	}
}
