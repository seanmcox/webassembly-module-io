/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I64Load32S extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I64Load32S(long align, long offset) {
		super(align, offset);
	}
}
