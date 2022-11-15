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
public class Branch implements Instruction {
	private Index labelIndex;

	/**
	 * @param labelIndex
	 */
	public Branch(Index labelIndex) {
		this.labelIndex = labelIndex;
	}

	/**
	 * @return the labelIndex
	 */
	public Index getLabelIndex() {
		return labelIndex;
	}
}
