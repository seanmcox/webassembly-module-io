/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.TableIndex;

/**
 * @author seanmcox
 *
 */
public class TableInitialize implements Instruction {
	private Index elementIndex;
	private TableIndex tableIndex;

	/**
	 * @param elementIndex
	 * @param tableIndex
	 */
	public TableInitialize(Index elementIndex, TableIndex tableIndex) {
		this.elementIndex = elementIndex;
		this.tableIndex = tableIndex;
	}

	/**
	 * @return the elementIndex
	 */
	public Index getElementIndex() {
		return elementIndex;
	}

	/**
	 * @return the tableIndex
	 */
	public TableIndex getTableIndex() {
		return tableIndex;
	}
}
