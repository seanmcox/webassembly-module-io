/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Store extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Store(long align, long offset) {
		super(align, offset);
	}
}
