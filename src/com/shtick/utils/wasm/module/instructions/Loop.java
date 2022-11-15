/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public class Loop implements Instruction {
	private BlockType blockType;
	private Instruction[] instructions;
	
	/**
	 * @param blockType
	 * @param instructions
	 */
	public Loop(BlockType blockType, Instruction[] instructions) {
		this.blockType = blockType;
		this.instructions = instructions;
	}

	/**
	 * @return the blockType
	 */
	public BlockType getBlockType() {
		return blockType;
	}

	/**
	 * @return the instructions
	 */
	public Instruction[] getInstructions() {
		return instructions;
	}
}
