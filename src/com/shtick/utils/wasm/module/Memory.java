/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
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
