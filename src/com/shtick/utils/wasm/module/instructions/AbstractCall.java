/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.FunctionIndex;

/**
 * @author seanmcox
 *
 */
public abstract class AbstractCall {
	/**
	 * @return the functionIndex
	 */
	public abstract FunctionIndex getFunctionIndex();
}
