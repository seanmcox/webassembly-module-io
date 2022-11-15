/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public abstract class VectorMemArgLaneInstruction extends VectorMemArgInstruction implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param align
	 * @param offset
	 * @param laneIndex
	 */
	public VectorMemArgLaneInstruction(long align, long offset, int laneIndex) {
		super(align, offset);
		this.laneIndex = laneIndex;
	}

	/**
	 * @return the laneIndex
	 */
	public int getLaneIndex() {
		return laneIndex;
	}
}
