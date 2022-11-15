/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Load8U extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Load8U(long align, long offset) {
		super(align, offset);
	}
}
