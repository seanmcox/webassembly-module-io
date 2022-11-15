/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32Load8S extends MemArgInstruction {

	/**
	 * @param align
	 * @param offset
	 */
	public I32Load8S(long align, long offset) {
		super(align, offset);
	}
}
