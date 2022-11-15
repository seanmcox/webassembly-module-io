/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Load extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Load(long align, long offset) {
		super(align, offset);
	}
}
