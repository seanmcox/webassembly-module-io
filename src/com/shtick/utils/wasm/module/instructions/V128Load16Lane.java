/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load16Lane extends VectorMemArgLaneInstruction {
	/**
	 * @param align
	 * @param offset
	 * @param laneIndex
	 */
	public V128Load16Lane(long align, long offset, int laneIndex) {
		super(align, offset, laneIndex);
	}
}
