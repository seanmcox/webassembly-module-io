/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-typesec
 * 
 * @author seanmcox
 *
 */
public class TypeSection extends Section {
	private Vector<FunctionType> functypes;

	public TypeSection(Vector<FunctionType> functypes) {
		this.functypes = functypes;
	}

	public Vector<FunctionType> getFunctypes() {
		return functypes;
	}
}
