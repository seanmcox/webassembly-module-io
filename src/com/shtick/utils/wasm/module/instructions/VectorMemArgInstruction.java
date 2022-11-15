/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public abstract class VectorMemArgInstruction extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public VectorMemArgInstruction(long align, long offset) {
		super(align, offset);
	}
}
