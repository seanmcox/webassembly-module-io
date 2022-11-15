/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load8Splat extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load8Splat(long align, long offset) {
		super(align, offset);
	}
}
