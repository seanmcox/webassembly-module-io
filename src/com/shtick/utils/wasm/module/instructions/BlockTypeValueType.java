/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.ValueType;

/**
 * @author seanmcox
 *
 */
public class BlockTypeValueType implements BlockType {
	private ValueType valueType;

	/**
	 * @param valueType
	 */
	public BlockTypeValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the valueType
	 */
	public ValueType getValueType() {
		return valueType;
	}
}
