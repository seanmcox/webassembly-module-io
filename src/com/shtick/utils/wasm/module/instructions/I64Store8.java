/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Store8 extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Store8(long align, long offset) {
		super(align, offset);
	}
}
