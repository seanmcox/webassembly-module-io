/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class F64Load extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public F64Load(long align, long offset) {
		super(align, offset);
	}
}
