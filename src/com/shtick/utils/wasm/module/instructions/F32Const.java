/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class F32Const implements Instruction {
	private float value;

	/**
	 * @param value
	 */
	public F32Const(float value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
}
