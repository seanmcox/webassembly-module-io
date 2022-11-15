/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load extends VectorMemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public V128Load(long align, long offset) {
		super(align, offset);
	}
}
