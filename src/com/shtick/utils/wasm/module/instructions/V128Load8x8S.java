/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load8x8S extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load8x8S(long align, long offset) {
		super(align, offset);
	}
}
