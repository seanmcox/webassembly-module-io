/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Store extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Store(long align, long offset) {
		super(align, offset);
	}
}
