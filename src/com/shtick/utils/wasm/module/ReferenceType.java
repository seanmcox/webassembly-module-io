package com.shtick.utils.wasm.module;

public enum ReferenceType implements ValueTypeInterface{
	REFTYPE_FUNCREF(0x070),REFTYPE_EXTERNREF(0x06F);
	private final int type;

	/**
	 * @param type
	 */
	private ReferenceType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
}
