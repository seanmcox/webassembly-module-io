/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-global
 * 
 * @author seanmcox
 *
 */
public class Global {
	private GlobalType globalType;
	private Expression expression;
	
	/**
	 * @param globalType
	 * @param expression
	 */
	public Global(GlobalType globalType, Expression expression) {
		this.globalType = globalType;
		this.expression = expression;
	}

	/**
	 * @return the globalType
	 */
	public GlobalType getGlobalType() {
		return globalType;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
}
