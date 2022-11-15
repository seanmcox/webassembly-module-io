/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.LinkedList;

/**
 * See: https://webassembly.github.io/spec/core/binary/instructions.html#binary-expr
 * See: https://webassembly.github.io/spec/core/syntax/instructions.html#syntax-expr
 * 
 * @author seanmcox
 *
 */
public class Expression {
	private LinkedList<Instruction> instructions;

	/**
	 * @param instructions
	 */
	public Expression(LinkedList<Instruction> instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return the instructions
	 */
	public LinkedList<Instruction> getInstructions() {
		return instructions;
	}
}
