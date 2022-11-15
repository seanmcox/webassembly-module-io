/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import java.util.Vector;

import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class BranchTable implements Instruction {
	private Vector<Index> caseIndices;
	private Index defaultIndex;
	
	/**
	 * @param caseIndices
	 * @param defaultIndex
	 */
	public BranchTable(Vector<Index> caseIndices, Index defaultIndex) {
		this.caseIndices = caseIndices;
		this.defaultIndex = defaultIndex;
	}

	/**
	 * @return the caseIndices
	 */
	public Vector<Index> getCaseIndices() {
		return caseIndices;
	}

	/**
	 * @return the defaultIndex
	 */
	public Index getDefaultIndex() {
		return defaultIndex;
	}
}
