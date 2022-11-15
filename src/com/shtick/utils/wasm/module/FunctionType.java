/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Objects;

/**
 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-functype
 * 
 * @author seanmcox
 *
 */
public class FunctionType {
	private ResultType resultType1;
	private ResultType resultType2;
	
	/**
	 * @param resultType1 Describes function parameters
	 * @param resultType2 Describes function results
	 */
	public FunctionType(ResultType resultType1, ResultType resultType2) {
		this.resultType1 = resultType1;
		this.resultType2 = resultType2;
	}

	/**
	 * @return the resultType1, describing function parameters
	 */
	public ResultType getResultType1() {
		return resultType1;
	}

	/**
	 * @return the resultType2, describing function results
	 */
	public ResultType getResultType2() {
		return resultType2;
	}

	@Override
	public String toString() {
		return resultType1.toString()+"->"+resultType2.toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return toString().equals(obj.toString());
	}
}
