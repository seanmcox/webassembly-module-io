/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * @author seanmcox
 *
 */
public interface LaneInstruction extends Instruction {
	/**
	 * 
	 * @return The Lane Index.
	 */
	int getLaneIndex();
}
