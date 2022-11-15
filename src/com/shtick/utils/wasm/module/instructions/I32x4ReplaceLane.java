/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I32x4ReplaceLane implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public I32x4ReplaceLane(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
