/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Store16 extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Store16(long align, long offset) {
		super(align, offset);
	}
}
