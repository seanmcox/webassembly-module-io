/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load8x8U extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load8x8U(long align, long offset) {
		super(align, offset);
	}
}
