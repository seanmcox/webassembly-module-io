/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
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
