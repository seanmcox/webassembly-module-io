/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.FunctionIndex;
import com.shtick.utils.wasm.module.Instruction;

/**
 * See: https://webassembly.github.io/spec/core/syntax/instructions.html#syntax-instr-ref
 * 
 * @author seanmcox
 *
 */
public class ReferenceFunction implements Instruction {
	private FunctionIndex functionIndex;

	/**
	 * @param functionIndex
	 */
	public ReferenceFunction(FunctionIndex functionIndex) {
		this.functionIndex = functionIndex;
	}

	/**
	 * @return the functionIndex
	 */
	public FunctionIndex getFunctionIndex() {
		return functionIndex;
	}
}
