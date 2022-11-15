/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.FunctionIndex;
import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class CallByIndex extends AbstractCall implements Instruction {
	private FunctionIndex functionIndex;

	/**
	 * @param functionIndex
	 */
	public CallByIndex(FunctionIndex functionIndex) {
		this.functionIndex = functionIndex;
	}

	/**
	 * @return the functionIndex
	 */
	public FunctionIndex getFunctionIndex() {
		return functionIndex;
	}
}
