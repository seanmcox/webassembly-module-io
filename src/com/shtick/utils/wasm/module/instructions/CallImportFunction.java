/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.FunctionIndex;
import com.shtick.utils.wasm.module.Import;
import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.TypeIndex;

/**
 * A version of Call intended to be used in in the instructions that make up a FunctionDefinition, presumably created before all function indexes have been resolved.
 * The Context will eventually have all of the function indexes resolved.
 * 
 * @author seanmcox
 *
 */
public class CallImportFunction extends AbstractCall implements Instruction {
	private Import imp;
	private Context context;

	/**
	 * @param functionDefinition
	 * @param context
	 */
	public CallImportFunction(Import imp, Context context) {
		if(!(imp.getImportDescriptor() instanceof TypeIndex))
			throw new RuntimeException("CallImportFunction can only be used on a function Import.");
		this.imp = imp;
		this.context = context;
	}

	/**
	 * @return the functionIndex
	 */
	public FunctionIndex getFunctionIndex() {
		FunctionIndex retval = context.getFunctionIndex(imp);
		if(retval == null)
			throw new RuntimeException("Import not found in Call context.");
		return retval;
	}
}
