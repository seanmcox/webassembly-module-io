/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import java.util.Vector;

import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.ValueType;

/**
 * @author seanmcox
 *
 */
public class Select implements Instruction {
	private Vector<ValueType> valueTypes;

	/**
	 * @param valueTypes Can be null.
	 */
	public Select(Vector<ValueType> valueTypes) {
		this.valueTypes = valueTypes;
	}

	/**
	 * @return the valueTypes
	 */
	public Vector<ValueType> getValueTypes() {
		return valueTypes;
	}
}
