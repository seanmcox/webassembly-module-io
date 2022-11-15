/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load16U extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load16U(long align, long offset) {
		super(align, offset);
	}
}
