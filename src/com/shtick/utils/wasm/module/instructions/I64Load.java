/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load(long align, long offset) {
		super(align, offset);
	}
}
