/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @see https://webassembly.github.io/spec/core/syntax/types.html#syntax-memtype
 * 
 * @author seanmcox
 *
 */
public class MemoryType implements ImportDescriptor {
	private Limits limits;

	/**
	 * @param limits
	 */
	public MemoryType(Limits limits) {
		this.limits = limits;
	}

	/**
	 * @return the limits
	 */
	public Limits getLimits() {
		return limits;
	}
}
