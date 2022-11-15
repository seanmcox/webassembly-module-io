/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
 * 
 * @author seanmcox
 *
 */
public class Code {
	private Vector<Locals> locals;
	private Expression expression;
	
	/**
	 * @param locals
	 * @param expression
	 */
	public Code(Vector<Locals> locals, Expression expression) {
		this.locals = locals;
		this.expression = expression;
	}

	/**
	 * @return the locals
	 */
	public Vector<Locals> getLocals() {
		return locals;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
}
