/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Store32 extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Store32(long align, long offset) {
		super(align, offset);
	}
}
