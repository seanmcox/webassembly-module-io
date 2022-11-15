/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Store32Lane extends VectorMemArgLaneInstruction {
	/**
	 * @param align
	 * @param offset
	 * @param laneIndex
	 */
	public V128Store32Lane(long align, long offset, int laneIndex) {
		super(align, offset, laneIndex);
	}
}
