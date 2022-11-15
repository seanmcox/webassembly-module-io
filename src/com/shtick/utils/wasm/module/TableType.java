/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-tabletype
 * 
 * @author seanmcox
 *
 */
public class TableType implements ImportDescriptor {
	private ReferenceType reftype;
	private Limits limits;
	
	/**
	 * @param reftype
	 * @param limits
	 */
	public TableType(ReferenceType reftype, Limits limits) {
		this.reftype = reftype;
		this.limits = limits;
	}

	/**
	 * @return the reftype
	 */
	public ReferenceType getReftype() {
		return reftype;
	}

	/**
	 * @return the limits
	 */
	public Limits getLimits() {
		return limits;
	}
}
