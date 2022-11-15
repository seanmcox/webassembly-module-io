/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * Push the value v128.const c to the stack.
 * 
 * @author seanmcox
 *
 */
public class V128Const implements Instruction {
	private final byte[] value;

	/**
	 * @param value An array of 16 bytes
	 */
	public V128Const(byte[] value) {
		if(value.length!=16)
			throw new RuntimeException("V128Const value must be exactly 16 bytes.");
		this.value = value;
	}

	/**
	 * 
	 * @param b0
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @param b5
	 * @param b6
	 * @param b7
	 * @param b8
	 * @param b9
	 * @param b10
	 * @param b11
	 * @param b12
	 * @param b13
	 * @param b14
	 * @param b15
	 */
	public V128Const(
		byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
		byte b8, byte b9, byte b10, byte b11, byte b12, byte b13, byte b14, byte b15
	) {
		value = new byte[] {
			b0,b1,b2,b3,b4,b5,b6,b7,
			b8,b9,b10,b11,b12,b13,b14,b15
		};
	}

	/**
	 * @return An array of 16 bytes
	 */
	public byte[] getValue() {
		return value;
	}
}
