/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public abstract class MemArgInstruction  implements Instruction{
	private long align;
	private long offset;
	
	/**
	 * @param align
	 * @param offset
	 */
	public MemArgInstruction(long align, long offset) {
		this.align = align;
		this.offset = offset;
	}

	/**
	 * @return the align
	 */
	public long getAlign() {
		return align;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}
}
