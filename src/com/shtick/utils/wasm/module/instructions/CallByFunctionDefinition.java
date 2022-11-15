/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.FunctionDefinition;
import com.shtick.utils.wasm.module.FunctionIndex;
import com.shtick.utils.wasm.module.Instruction;

/**
 * A version of Call intended to be used in in the instructions that make up a FunctionDefinition, presumably created before all function indexes have been resolved.
 * The Context will eventually have all of the function indexes resolved.
 * 
 * @author seanmcox
 *
 */
public class CallByFunctionDefinition extends AbstractCall implements Instruction {
	private FunctionDefinition functionDefinition;
	private Context context;

	/**
	 * @param functionDefinition
	 * @param context
	 */
	public CallByFunctionDefinition(FunctionDefinition functionDefinition, Context context) {
		this.functionDefinition = functionDefinition;
		this.context = context;
	}

	/**
	 * @return the functionDefinition
	 */
	public FunctionDefinition getFunctionDefinition() {
		return functionDefinition;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return the functionIndex
	 */
	public FunctionIndex getFunctionIndex() {
		FunctionIndex retval = context.getFunctionIndex(functionDefinition);
		if(retval == null)
			throw new RuntimeException("FunctionDefinition not found in Call context.");
		return retval;
	}
}
