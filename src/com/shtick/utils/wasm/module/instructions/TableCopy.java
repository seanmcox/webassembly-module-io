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
public class TableCopy implements Instruction {
	private TableIndex tableIndexX;
	private TableIndex tableIndexY;
	
	/**
	 * @param tableIndexX
	 * @param tableIndexY
	 */
	public TableCopy(TableIndex tableIndexX, TableIndex tableIndexY) {
		this.tableIndexX = tableIndexX;
		this.tableIndexY = tableIndexY;
	}

	/**
	 * @return the tableIndexX
	 */
	public TableIndex getTableIndexX() {
		return tableIndexX;
	}

	/**
	 * @return the tableIndexY
	 */
	public TableIndex getTableIndexY() {
		return tableIndexY;
	}
}
