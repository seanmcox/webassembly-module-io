/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class FunctionDefinition {
	private FunctionType functionType;
	private Code code;
	
	/**
	 * @param functionType
	 * @param code
	 */
	public FunctionDefinition(FunctionType functionType, Code code) {
		this.functionType = functionType;
		this.code = code;
	}

	/**
	 * @return the functionType
	 */
	public FunctionType getFunctionType() {
		return functionType;
	}

	/**
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}
}
