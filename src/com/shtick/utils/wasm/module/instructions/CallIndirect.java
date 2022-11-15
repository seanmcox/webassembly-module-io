/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.TableIndex;
import com.shtick.utils.wasm.module.TypeIndex;

/**
 * @author seanmcox
 *
 */
public class CallIndirect implements Instruction {
	private TypeIndex typeIndex;
	private TableIndex tableIndex;
	
	/**
	 * @param typeIndex
	 * @param tableIndex
	 */
	public CallIndirect(TypeIndex typeIndex, TableIndex tableIndex) {
		this.typeIndex = typeIndex;
		this.tableIndex = tableIndex;
	}

	/**
	 * @return the typeIndex
	 */
	public TypeIndex getTypeIndex() {
		return typeIndex;
	}

	/**
	 * @return the tableIndex
	 */
	public TableIndex getTableIndex() {
		return tableIndex;
	}
}
