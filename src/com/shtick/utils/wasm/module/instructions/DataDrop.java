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
public class DataDrop implements Instruction {
	private Index dataIndex;

	/**
	 * @param dataIndex
	 */
	public DataDrop(Index dataIndex) {
		this.dataIndex = dataIndex;
	}

	/**
	 * @return the dataIndex
	 */
	public Index getDataIndex() {
		return dataIndex;
	}
}
