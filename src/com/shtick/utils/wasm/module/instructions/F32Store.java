/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class F32Store extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public F32Store(long align, long offset) {
		super(align, offset);
	}
}
