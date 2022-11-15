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
public class ElementDrop implements Instruction {
	private Index elementIndex;

	/**
	 * @param elementIndex
	 */
	public ElementDrop(Index elementIndex) {
		this.elementIndex = elementIndex;
	}

	/**
	 * @return the elementIndex
	 */
	public Index getElementIndex() {
		return elementIndex;
	}
}
