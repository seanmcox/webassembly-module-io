/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-table
 * 
 * @author seanmcox
 *
 */
public class Table {
	private TableType tableType;

	/**
	 * @param tableType
	 */
	public Table(TableType tableType) {
		this.tableType = tableType;
	}

	/**
	 * @return the tableType
	 */
	public TableType getTableType() {
		return tableType;
	}
}
