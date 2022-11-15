/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class GlobalType implements ImportDescriptor {
	private ValueType valueType;
	private Mutability mutability;
	
	/**
	 * @param valueType
	 * @param mutability
	 */
	public GlobalType(ValueType valueType, Mutability mutability) {
		this.valueType = valueType;
		this.mutability = mutability;
	}

	/**
	 * @return the valueType
	 */
	public ValueType getValueType() {
		return valueType;
	}

	/**
	 * @return the mutability
	 */
	public Mutability getMutability() {
		return mutability;
	}
}
