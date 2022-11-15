/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.GlobalIndex;
import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class GlobalGet implements Instruction {
	private GlobalIndex globalIndex;

	/**
	 * @param globalIndex
	 */
	public GlobalGet(GlobalIndex globalIndex) {
		this.globalIndex = globalIndex;
	}

	/**
	 * @return the globalIndex
	 */
	public GlobalIndex getGlobalIndex() {
		return globalIndex;
	}
}
