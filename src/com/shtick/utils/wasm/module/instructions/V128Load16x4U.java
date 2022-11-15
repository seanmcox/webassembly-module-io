/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load16x4U extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load16x4U(long align, long offset) {
		super(align, offset);
	}
}
