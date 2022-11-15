/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-export
 * 
 * @author seanmcox
 *
 */
public class Export {
	private String name;
	private ExportDescriptor exportDescriptor;
	
	/**
	 * @param name
	 * @param exportDescriptor
	 */
	public Export(String name, ExportDescriptor exportDescriptor) {
		this.name = name;
		this.exportDescriptor = exportDescriptor;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the exportDescriptor
	 */
	public ExportDescriptor getExportDescriptor() {
		return exportDescriptor;
	}
}
