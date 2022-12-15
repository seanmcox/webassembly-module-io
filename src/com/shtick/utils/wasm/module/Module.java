/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.LinkedList;
import java.util.Vector;

/**
 * The serialization is defined here:
 * https://webassembly.github.io/spec/core/binary/modules.html
 * 
 * @author seanmcox
 *
 */
public class Module {
	private LinkedList<CustomSection> typeMetaSections;
	private TypeSection typeSection;
	private LinkedList<CustomSection> importMetaSections;
	private ImportSection importSection;
	private LinkedList<CustomSection> functionMetaSections;
	private FunctionSection functionSection;
	private LinkedList<CustomSection> tableMetaSections;
	private TableSection tableSection;
	private LinkedList<CustomSection> memoryMetaSections;
	private MemorySection memorySection;
	private LinkedList<CustomSection> globalMetaSections;
	private GlobalSection globalSection;
	private LinkedList<CustomSection> exportMetaSections;
	private ExportSection exportSection;
	private LinkedList<CustomSection> startMetaSections;
	private StartSection startSection;
	private LinkedList<CustomSection> elementMetaSections;
	private ElementSection elementSection;
	private LinkedList<CustomSection> dataCountMetaSections;
	private DataCountSection dataCountSection;
	private LinkedList<CustomSection> codeMetaSections;
	private CodeSection codeSection;
	private LinkedList<CustomSection> dataMetaSections;
	private DataSection dataSection;
	private LinkedList<CustomSection> moduleMetaSections;
	
	/**
	 * 
	 * @param context An object containing ordered definitions. The orders will be preserved, so that indexes into these definitions will be the same as the final indexes. 
	 * @param typeMetaSections
	 * @param importMetaSections
	 * @param functionMetaSections
	 * @param tableMetaSections
	 * @param memoryMetaSections
	 * @param globalMetaSections
	 * @param exportMetaSections
	 * @param exportSection
	 * @param startMetaSections
	 * @param startSection
	 * @param elementMetaSections
	 * @param elementSection
	 * @param dataCountMetaSections
	 * @param codeMetaSections
	 * @param dataMetaSections
	 * @param moduleMetaSections
	 */
	public Module(Context context,
			LinkedList<CustomSection> typeMetaSections,
			LinkedList<CustomSection> importMetaSections,
			LinkedList<CustomSection> functionMetaSections,
			LinkedList<CustomSection> tableMetaSections,
			LinkedList<CustomSection> memoryMetaSections,
			LinkedList<CustomSection> globalMetaSections,
			LinkedList<CustomSection> exportMetaSections, ExportSection exportSection,
			LinkedList<CustomSection> startMetaSections, StartSection startSection,
			LinkedList<CustomSection> elementMetaSections, ElementSection elementSection,
			LinkedList<CustomSection> dataCountMetaSections,
			LinkedList<CustomSection> codeMetaSections,
			LinkedList<CustomSection> dataMetaSections,
			LinkedList<CustomSection> moduleMetaSections) {
		Vector<FunctionType> functionTypes = new Vector<>();
		Vector<TypeIndex> typeIndices = new Vector<>();
		Vector<Code> codes = new Vector<>();
		Vector<Import> imports = new Vector<>();
		Vector<Memory> memories = new Vector<>();
		Vector<Global> globals = new Vector<>();
		Vector<Table> tables = new Vector<>();
		for(FunctionType functionType:context.getFunctionTypes())
			functionTypes.add(functionType);
		for(FunctionDefinition functionDefinition:context.getFunctionDefinitions()) {
			FunctionType functionType = functionDefinition.getFunctionType();
			typeIndices.add(context.getTypeIndex(functionType));
			codes.add(functionDefinition.getCode());
		}
		for(Import imp:context.getImports())
			imports.add(imp);
		for(Memory m:context.getMemories())
			memories.add(m);
		for(Global g:context.getGlobals())
			globals.add(g);
		for(Table t:context.getTables())
			tables.add(t);
		Vector<Data> data = new Vector<>();
		for(Data d:context.getData())
			data.add(d);
		DataSection dataSection = new DataSection(data);
		this.typeMetaSections = typeMetaSections;
		this.typeSection = new TypeSection(functionTypes);
		this.importMetaSections = importMetaSections;
		this.importSection = new ImportSection(imports);
		this.functionMetaSections = functionMetaSections;
		this.functionSection = new FunctionSection(typeIndices);
		this.tableMetaSections = tableMetaSections;
		this.tableSection = new TableSection(tables);
		this.memoryMetaSections = memoryMetaSections;
		this.memorySection = new MemorySection(memories);
		this.globalMetaSections = globalMetaSections;
		this.globalSection = new GlobalSection(globals);
		this.exportMetaSections = exportMetaSections;
		this.exportSection = exportSection;
		this.startMetaSections = startMetaSections;
		this.startSection = startSection;
		this.elementMetaSections = elementMetaSections;
		this.elementSection = elementSection;
		this.dataCountMetaSections = dataCountMetaSections;
		this.dataCountSection = new DataCountSection(dataSection.getData().size());
		this.codeMetaSections = codeMetaSections;
		this.codeSection = new CodeSection(codes);
		this.dataMetaSections = dataMetaSections;
		this.dataSection = dataSection;
		this.moduleMetaSections = moduleMetaSections;
	}
	
	/**
	 * @param typeMetaSections
	 * @param typeSections
	 * @param importMetaSections
	 * @param importSections
	 * @param functionMetaSections
	 * @param functionSections
	 * @param tableMetaSections
	 * @param tableSections
	 * @param memoryMetaSections
	 * @param memorySections
	 * @param globalMetaSections
	 * @param globalSections
	 * @param exportMetaSections
	 * @param exportSections
	 * @param startMetaSections
	 * @param startSection
	 * @param elementMetaSections
	 * @param elementSections
	 * @param dataCountMetaSections
	 * @param codeMetaSections
	 * @param codeSections
	 * @param dataMetaSections
	 * @param dataSections
	 * @param moduleMetaSections
	 */
	public Module(LinkedList<CustomSection> typeMetaSections, TypeSection typeSection,
			LinkedList<CustomSection> importMetaSections, ImportSection importSection,
			LinkedList<CustomSection> functionMetaSections, FunctionSection functionSection,
			LinkedList<CustomSection> tableMetaSections, TableSection tableSection,
			LinkedList<CustomSection> memoryMetaSections, MemorySection memorySection,
			LinkedList<CustomSection> globalMetaSections, GlobalSection globalSection,
			LinkedList<CustomSection> exportMetaSections, ExportSection exportSection,
			LinkedList<CustomSection> startMetaSections, StartSection startSection,
			LinkedList<CustomSection> elementMetaSections, ElementSection elementSection,
			LinkedList<CustomSection> dataCountMetaSections,
			LinkedList<CustomSection> codeMetaSections, CodeSection codeSection,
			LinkedList<CustomSection> dataMetaSections, DataSection dataSection,
			LinkedList<CustomSection> moduleMetaSections) {
		this.typeMetaSections = typeMetaSections;
		this.typeSection = typeSection;
		this.importMetaSections = importMetaSections;
		this.importSection = importSection;
		this.functionMetaSections = functionMetaSections;
		this.functionSection = functionSection;
		this.tableMetaSections = tableMetaSections;
		this.tableSection = tableSection;
		this.memoryMetaSections = memoryMetaSections;
		this.memorySection = memorySection;
		this.globalMetaSections = globalMetaSections;
		this.globalSection = globalSection;
		this.exportMetaSections = exportMetaSections;
		this.exportSection = exportSection;
		this.startMetaSections = startMetaSections;
		this.startSection = startSection;
		this.elementMetaSections = elementMetaSections;
		this.elementSection = elementSection;
		this.dataCountMetaSections = dataCountMetaSections;
		this.dataCountSection = new DataCountSection(dataSection.getData().size());
		this.codeMetaSections = codeMetaSections;
		this.codeSection = codeSection;
		this.dataMetaSections = dataMetaSections;
		this.dataSection = dataSection;
		this.moduleMetaSections = moduleMetaSections;
	}

	/**
	 * 
	 * @param typeSection
	 * @param importSection
	 * @param functionSection
	 * @param tableSection
	 * @param memorySection
	 * @param globalSection
	 * @param exportSection
	 * @param startSection
	 * @param elementSection
	 * @param codeSection
	 * @param dataSection
	 */
	public Module(TypeSection typeSection, ImportSection importSection, FunctionSection functionSection, TableSection tableSection, MemorySection memorySection,
			GlobalSection globalSection, ExportSection exportSection, StartSection startSection, ElementSection elementSection,
			CodeSection codeSection, DataSection dataSection) {
		this(new LinkedList<>(),typeSection,new LinkedList<>(),importSection,new LinkedList<>(),functionSection,new LinkedList<>(),tableSection,new LinkedList<>(),
			memorySection,new LinkedList<>(),globalSection,new LinkedList<>(),exportSection,new LinkedList<>(),startSection,new LinkedList<>(),elementSection,
			new LinkedList<>(),new LinkedList<>(),codeSection,new LinkedList<>(),dataSection,new LinkedList<>()
		);
	}

	/**
	 * 
	 * @param context
	 * @param exportSection
	 * @param startSection
	 * @param elementSection
	 */
	public Module(Context context, ExportSection exportSection, StartSection startSection, ElementSection elementSection) {
		this(context, new LinkedList<>(),new LinkedList<>(),new LinkedList<>(),new LinkedList<>(),new LinkedList<>(),
			new LinkedList<>(),new LinkedList<>(),exportSection,new LinkedList<>(),startSection,new LinkedList<>(),elementSection,
			new LinkedList<>(),new LinkedList<>(),new LinkedList<>(),new LinkedList<>()
		);
	}
	
	public LinkedList<CustomSection> getTypeMetaSections() {
		return typeMetaSections;
	}
	public TypeSection getTypeSection() {
		return typeSection;
	}
	public LinkedList<CustomSection> getImportMetaSections() {
		return importMetaSections;
	}
	public ImportSection getImportSection() {
		return importSection;
	}
	public LinkedList<CustomSection> getFunctionMetaSections() {
		return functionMetaSections;
	}
	public FunctionSection getFunctionSection() {
		return functionSection;
	}
	public LinkedList<CustomSection> getTableMetaSections() {
		return tableMetaSections;
	}
	public TableSection getTableSection() {
		return tableSection;
	}
	public LinkedList<CustomSection> getMemoryMetaSections() {
		return memoryMetaSections;
	}
	public MemorySection getMemorySection() {
		return memorySection;
	}
	public LinkedList<CustomSection> getGlobalMetaSections() {
		return globalMetaSections;
	}
	public GlobalSection getGlobalSection() {
		return globalSection;
	}
	public LinkedList<CustomSection> getExportMetaSections() {
		return exportMetaSections;
	}
	public ExportSection getExportSection() {
		return exportSection;
	}
	public LinkedList<CustomSection> getStartMetaSections() {
		return startMetaSections;
	}
	public StartSection getStartSection() {
		return startSection;
	}
	public LinkedList<CustomSection> getElementMetaSections() {
		return elementMetaSections;
	}
	public ElementSection getElementSection() {
		return elementSection;
	}
	public LinkedList<CustomSection> getDataCountMetaSections() {
		return dataCountMetaSections;
	}
	public DataCountSection getDataCountSection() {
		return dataCountSection;
	}
	public LinkedList<CustomSection> getCodeMetaSections() {
		return codeMetaSections;
	}
	public CodeSection getCodeSection() {
		return codeSection;
	}
	public LinkedList<CustomSection> getDataMetaSections() {
		return dataMetaSections;
	}
	public DataSection getDataSection() {
		return dataSection;
	}
	public LinkedList<CustomSection> getModuleMetaSections() {
		return moduleMetaSections;
	}
}
