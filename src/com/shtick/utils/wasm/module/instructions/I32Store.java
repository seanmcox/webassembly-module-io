/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Store extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Store(long align, long offset) {
		super(align, offset);
	}
}
