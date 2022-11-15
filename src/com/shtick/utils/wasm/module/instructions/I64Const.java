/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class I64Const implements Instruction {
	private long value;

	/**
	 * @param value
	 */
	public I64Const(long value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}
}
