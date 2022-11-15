/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import java.util.Vector;

import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.FunctionType;

/**
 * @author seanmcox
 *
 */
public class BlockTypeIndex implements BlockType {
	private long typeIndex;
	
	/**
	 * 
	 * @param functionType
	 * @param context
	 */
	public BlockTypeIndex(FunctionType functionType, Context context) {
		typeIndex = context.getTypeIndex(functionType).getIndex();
	}

	/**
	 * @param typeIndex
	 */
	public BlockTypeIndex(long typeIndex) {
		this.typeIndex = typeIndex;
	}

	/**
	 * @return the typeIndex
	 */
	public long getTypeIndex() {
		return typeIndex;
	}
}
