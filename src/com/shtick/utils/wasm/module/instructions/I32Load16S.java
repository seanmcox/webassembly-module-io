/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Load16S extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Load16S(long align, long offset) {
		super(align, offset);
	}
}
