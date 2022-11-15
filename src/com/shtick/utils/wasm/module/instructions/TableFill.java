/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.TableIndex;

/**
 * @author seanmcox
 *
 */
public class TableFill implements Instruction {
	private TableIndex tableIndex;

	/**
	 * @param tableIndex
	 */
	public TableFill(TableIndex tableIndex) {
		this.tableIndex = tableIndex;
	}

	/**
	 * @return the tableIndex
	 */
	public TableIndex getTableIndex() {
		return tableIndex;
	}
}
