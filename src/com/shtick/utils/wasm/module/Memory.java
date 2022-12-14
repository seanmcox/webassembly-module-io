/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @see https://webassembly.github.io/spec/core/syntax/modules.html#syntax-mem
 * 
 * @author seanmcox
 *
 */
public class Memory {
	private MemoryType memoryType;

	/**
	 * @param memoryType
	 */
	public Memory(MemoryType memoryType) {
		this.memoryType = memoryType;
	}

	/**
	 * @return the memoryType
	 */
	public MemoryType getMemoryType() {
		return memoryType;
	}
}
