package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-table
 * 
 * @author seanmcox
 *
 */
public class TableSection extends Section {
	private Vector<Table> tables;

	public TableSection(Vector<Table> tables) {
		this.tables = tables;
	}

	/**
	 * @return the tables
	 */
	public Vector<Table> getTables() {
		return tables;
	}
}
