/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Store8 extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Store8(long align, long offset) {
		super(align, offset);
	}
}
