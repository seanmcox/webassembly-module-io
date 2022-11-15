/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * @author seanmcox
 *
 */
public class ImportSection extends Section {
	private Vector<Import> imports;

	public ImportSection(Vector<Import> imports) {
		super();
		this.imports = imports;
	}

	public Vector<Import> getImports() {
		return imports;
	}
}
