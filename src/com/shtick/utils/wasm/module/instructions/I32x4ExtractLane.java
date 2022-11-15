/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32x4ExtractLane implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public I32x4ExtractLane(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
