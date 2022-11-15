/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class Start {
	private FunctionIndex functionIndex;

	/**
	 * @param functionIndex
	 */
	public Start(FunctionIndex functionIndex) {
		this.functionIndex = functionIndex;
	}

	/**
	 * @return the functionIndex
	 */
	public FunctionIndex getFunctionIndex() {
		return functionIndex;
	}
}
