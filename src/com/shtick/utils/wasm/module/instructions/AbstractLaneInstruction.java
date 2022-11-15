/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public abstract class AbstractLaneInstruction implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public AbstractLaneInstruction(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
