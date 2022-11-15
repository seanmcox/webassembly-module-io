/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class LocalSet implements Instruction {
	private Index localIndex;

	/**
	 * @param localIndex
	 */
	public LocalSet(Index localIndex) {
		this.localIndex = localIndex;
	}

	/**
	 * @return the localIndex
	 */
	public Index getLocalIndex() {
		return localIndex;
	}
}
