/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.ReferenceType;

/**
 * See: https://webassembly.github.io/spec/core/syntax/instructions.html#syntax-instr-ref
 * 
 * @author seanmcox
 *
 */
public class ReferenceNull implements Instruction {
	private ReferenceType referenceType;

	/**
	 * @param referenceType
	 */
	public ReferenceNull(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * @return the referenceType
	 */
	public ReferenceType getReferenceType() {
		return referenceType;
	}
}
