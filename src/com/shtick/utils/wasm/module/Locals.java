/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
 * 
 * @author seanmcox
 *
 */
public class Locals {
	private int count;
	private ValueType valueType;
	
	/**
	 * @param count
	 * @param valueType
	 */
	public Locals(int count, ValueType valueType) {
		this.count = count;
		this.valueType = valueType;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the valueType
	 */
	public ValueType getValueType() {
		return valueType;
	}
}
