/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load32U extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load32U(long align, long offset) {
		super(align, offset);
	}
}
