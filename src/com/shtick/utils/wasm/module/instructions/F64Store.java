/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class F64Store extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public F64Store(long align, long offset) {
		super(align, offset);
	}
}
