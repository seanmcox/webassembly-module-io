/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Store16 extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Store16(long align, long offset) {
		super(align, offset);
	}
}
