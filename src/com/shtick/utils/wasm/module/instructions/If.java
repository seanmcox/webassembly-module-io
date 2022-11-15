/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class If implements Instruction {
	private BlockType blockType;
	private Instruction[] ifInstructions;
	private Instruction[] elseInstructions;
	
	/**
	 * @param blockType
	 * @param ifInstructions
	 * @param elseInstructions
	 */
	public If(BlockType blockType, Instruction[] ifInstructions, Instruction[] elseInstructions) {
		this.blockType = blockType;
		this.ifInstructions = ifInstructions;
		this.elseInstructions = elseInstructions;
	}

	/**
	 * @return the blockType
	 */
	public BlockType getBlockType() {
		return blockType;
	}

	/**
	 * @return the ifInstructions
	 */
	public Instruction[] getIfInstructions() {
		return ifInstructions;
	}

	/**
	 * @return the elseInstructions
	 */
	public Instruction[] getElseInstructions() {
		return elseInstructions;
	}
}
