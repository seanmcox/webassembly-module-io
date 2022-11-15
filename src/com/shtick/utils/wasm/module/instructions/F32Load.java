/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class F32Load extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public F32Load(long align, long offset) {
		super(align, offset);
	}
}
