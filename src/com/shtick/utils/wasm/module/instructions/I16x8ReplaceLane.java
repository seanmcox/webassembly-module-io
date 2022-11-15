/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I16x8ReplaceLane implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public I16x8ReplaceLane(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
