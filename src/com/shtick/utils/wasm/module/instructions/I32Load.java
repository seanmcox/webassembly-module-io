/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @see https://webassembly.github.io/spec/core/exec/instructions.html#memory-instructions
 * 
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
