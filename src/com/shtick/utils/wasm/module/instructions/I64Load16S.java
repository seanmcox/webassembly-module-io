/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load16S extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load16S(long align, long offset) {
		super(align, offset);
	}
}
