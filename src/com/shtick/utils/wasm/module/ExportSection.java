/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @see https://webassembly.github.io/spec/core/syntax/modules.html#syntax-export
 * 
 * Note, that at this time, the Webassembly specification only supports exporting one memory buffer,
 * and the name of the exported buffer seems to always be "memory".
 * To describe multiple areas of memory, the standard practice seems to be to export globals which
 * are indexes into the exported buffer.
 * 
 * @see https://webassembly.github.io/spec/core/syntax/modules.html#syntax-datamode
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
