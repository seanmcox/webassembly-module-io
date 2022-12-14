/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-func
 * 
 * @author seanmcox
 *
 */
public class Code {
	private Vector<Locals> locals;
	private Expression expression;
	
	/**
	 * @param locals A description of local variable.
	 *               The passed into the function are not described here, though the parameters are indexed
	 *               first as local variables, before the locals declared here.
	 *               Hence, if there are no parameters, then the first local described here is the 0th local,
	 *               but if there are two parameters, then the first local described here is the 2nd local.
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
