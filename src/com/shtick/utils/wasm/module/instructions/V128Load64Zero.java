/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load64Zero extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load64Zero(long align, long offset) {
		super(align, offset);
	}
}
