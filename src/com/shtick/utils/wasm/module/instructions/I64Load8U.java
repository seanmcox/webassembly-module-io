/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load8U extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load8U(long align, long offset) {
		super(align, offset);
	}
}
