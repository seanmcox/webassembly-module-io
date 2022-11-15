/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public enum Mutability {
	CONSTANT(0),VARIABLE(1);
	private final int mutability;

	/**
	 * @param type
	 */
	private Mutability(int mutability) {
		this.mutability = mutability;
	}

	/**
	 * @return the type
	 */
	public int getMutability() {
		return mutability;
	}
}
