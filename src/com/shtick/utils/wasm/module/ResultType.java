/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;
import java.util.stream.Collectors;

/**
 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-resulttype
 * 
 * @author seanmcox
 *
 */
public class ResultType {
	private Vector<ValueType> valtypes;

	/**
	 * @param valtypes
	 */
	public ResultType(Vector<ValueType> valtypes) {
		this.valtypes = valtypes;
	}

	/**
	 * @return the valtypes
	 */
	public Vector<ValueType> getValtypes() {
		return valtypes;
	}

	@Override
	public String toString() {
		String list = valtypes.stream().map(vt -> ""+vt.getType()).collect(Collectors.joining(","));
		return list;
	}
}
