/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-valtype
 * 
 * @author seanmcox
 *
 */
public enum ValueType implements ValueTypeInterface{
	NUMTYPE_I32(0x07F),NUMTYPE_I64(0x07E),NUMTYPE_F32(0x07D),NUMTYPE_F64(0x07C),VECTYPE_V128(0x07B),REFTYPE_FUNCREF(ReferenceType.REFTYPE_FUNCREF.getType()),REFTYPE_EXTERNREF(ReferenceType.REFTYPE_EXTERNREF.getType());
	private final int type;

	/**
	 * @param type
	 */
	private ValueType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
}
