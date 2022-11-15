package com.shtick.utils.wasm.module;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Vector;

import com.shtick.utils.wasm.module.instructions.*;

public class ModuleSerializer {
	static final byte[] MAGIC_NUMBER = new byte[] {0x00, 0x61, 0x73, 0x6D};
	static final byte[] VERSION_NUMBER = new byte[] {0x01, 0x00, 0x00, 0x00};

	static final int CUSTOM_SECTION_ID = 0;
	static final int TYPE_SECTION_ID = 1;
	static final int IMPORT_SECTION_ID = 2;
	static final int FUNCTION_SECTION_ID = 3;
	static final int TABLE_SECTION_ID = 4;
	static final int MEMORY_SECTION_ID = 5;
	static final int GLOBAL_SECTION_ID = 6;
	static final int EXPORT_SECTION_ID = 7;
	static final int START_SECTION_ID = 8;
	static final int ELEMENT_SECTION_ID = 9;
	static final int CODE_SECTION_ID = 10;
	static final int DATA_SECTION_ID = 11;
	static final int DATA_COUNT_SECTION_ID = 12;

	public static void writeModule(Module module, OutputStream out) throws IOException {
		out.write(MAGIC_NUMBER);
		out.write(VERSION_NUMBER);
		for(CustomSection section:module.getTypeMetaSections())
			writeCustomSection(section, out);
		writeTypeSection(module.getTypeSection(), out);
		for(CustomSection section:module.getImportMetaSections())
			writeCustomSection(section, out);
		writeImportSection(module.getImportSection(), out);
		for(CustomSection section:module.getFunctionMetaSections())
			writeCustomSection(section, out);
		writeFunctionSection(module.getFunctionSection(), out);
		for(CustomSection section:module.getTableMetaSections())
			writeCustomSection(section, out);
		writeTableSection(module.getTableSection(), out);
		for(CustomSection section:module.getMemoryMetaSections())
			writeCustomSection(section, out);
		writeMemorySection(module.getMemorySection(), out);
		for(CustomSection section:module.getGlobalMetaSections())
			writeCustomSection(section, out);
		writeGlobalSection(module.getGlobalSection(), out);
		for(CustomSection section:module.getExportMetaSections())
			writeCustomSection(section, out);
		writeExportSection(module.getExportSection(), out);
		for(CustomSection section:module.getStartMetaSections())
			writeCustomSection(section, out);
		if(module.getStartSection()!=null)
			writeStartSection(module.getStartSection(), out);
		for(CustomSection section:module.getElementMetaSections())
			writeCustomSection(section, out);
		writeElementSection(module.getElementSection(), out);
		for(CustomSection section:module.getDataCountMetaSections())
			writeCustomSection(section, out);
		if(module.getDataCountSection()!=null)
			writeDataCountSection(module.getDataCountSection(), out);
		for(CustomSection section:module.getCodeMetaSections())
			writeCustomSection(section, out);
		writeCodeSection(module.getCodeSection(), out);
		for(CustomSection section:module.getDataMetaSections())
			writeCustomSection(section, out);
		writeDataSection(module.getDataSection(), out);
		for(CustomSection section:module.getModuleMetaSections())
			writeCustomSection(section, out);
	}
	
	/***************************
	 * 
	 * Section writing routines
	 * 
	 ***************************/
		
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-section
	 * @param sectionID
	 * @param sectionDataSize
	 * @param out
	 * @throws IOException
	 */
	private static void writeSectionHeader(int sectionID, long sectionDataSize, OutputStream out) throws IOException {
		out.write(sectionID);
		writeUnsignedLEB128(sectionDataSize, out);
	}
	
	private static void writeCustomSection(CustomSection section, OutputStream out) throws IOException {
		writeSectionHeader(CUSTOM_SECTION_ID, section.getData().length, out);
		out.write(section.getData());
	}
	
	private static void writeTypeSection(TypeSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getFunctypes(), ModuleSerializer::writeFunctype, bout);
		writeSectionHeader(TYPE_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-importsec
	 * 
	 * @param section
	 * @param out
	 * @throws IOException
	 */
	private static void writeImportSection(ImportSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getImports(), ModuleSerializer::writeImport, bout);
		writeSectionHeader(IMPORT_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-funcsec
	 * 
	 * @param section
	 * @param out
	 * @throws IOException
	 */
	private static void writeFunctionSection(FunctionSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getTypeidxes(), ModuleSerializer::writeIndex, bout);
		writeSectionHeader(FUNCTION_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeTableSection(TableSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getTables(), ModuleSerializer::writeTable, bout);
		writeSectionHeader(TABLE_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeMemorySection(MemorySection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getMems(), ModuleSerializer::writeMemory, bout);
		writeSectionHeader(MEMORY_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeGlobalSection(GlobalSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getGlobals(), ModuleSerializer::writeGlobal, bout);
		writeSectionHeader(GLOBAL_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeExportSection(ExportSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getExports(), ModuleSerializer::writeExport, bout);
		writeSectionHeader(EXPORT_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeStartSection(StartSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeStart(section.getStart(),bout);
		writeSectionHeader(START_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeElementSection(ElementSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getElems(), ModuleSerializer::writeElement, bout);
		writeSectionHeader(ELEMENT_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeDataCountSection(DataCountSection section, OutputStream out) throws IOException {
		writeSectionHeader(DATA_COUNT_SECTION_ID, getUnsignedLEB128Size(section.getDataCount()), out);
		writeUnsignedLEB128(section.getDataCount(), out);
	}
	
	private static void writeCodeSection(CodeSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getCodes(), ModuleSerializer::writeCode, bout);
		writeSectionHeader(CODE_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	private static void writeDataSection(DataSection section, OutputStream out) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(section.getData(), ModuleSerializer::writeData, bout);
		writeSectionHeader(DATA_SECTION_ID, bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	/********************************************
	 * 
	 * Routines for writing sub-section elements
	 * 
	 ********************************************/

	private static void writeFunctype(FunctionType functionType, OutputStream out) throws IOException{
		out.write(0x060);
		writeResultType(functionType.getResultType1(),out);
		writeResultType(functionType.getResultType2(),out);
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-resulttype
	 * 
	 * @param resultType
	 * @param out
	 * @throws IOException
	 */
	private static void writeResultType(ResultType resultType, OutputStream out) throws IOException{
		writeVector(resultType.getValtypes(), ModuleSerializer::writeValueType, out);
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-valtype
	 * 
	 * @param valueType
	 * @param out
	 * @throws IOException
	 */
	private static void writeValueType(ValueTypeInterface valueType, OutputStream out) throws IOException{
		out.write(valueType.getType());
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-importsec
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeImport(Import datum, OutputStream out) throws IOException{
		writeName(datum.getModule(),out);
		writeName(datum.getName(),out);
		ImportDescriptor importDescriptor = datum.getImportDescriptor();
		if(importDescriptor instanceof TypeIndex) {
			out.write(0);
			writeIndex((TypeIndex)importDescriptor, out);
		}
		else if(importDescriptor instanceof TableType) {
			out.write(1);
			writeTableType((TableType)importDescriptor, out);
		}
		else if(importDescriptor instanceof MemoryType) {
			out.write(2);
			writeMemoryType((MemoryType)importDescriptor, out);
		}
		else if(importDescriptor instanceof GlobalType) {
			out.write(3);
			writeGlobalType((GlobalType)importDescriptor, out);
		}
		else {
			throw new RuntimeException("ImportDescriptor found of unexpected type, "+importDescriptor.getClass().getCanonicalName());
		}
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/values.html#binary-name
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeName(String datum, OutputStream out) throws IOException{
		byte[] bytes = datum.getBytes("UTF8");
		writeUnsignedLEB128(bytes.length, out);
		out.write(bytes);
	}
	
	private static void writeIndex(Index datum, OutputStream out) throws IOException{
		writeUnsignedLEB128(datum.getIndex(), out);
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-tabletype
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeTableType(TableType datum, OutputStream out) throws IOException{
		writeValueType(datum.getReftype(),out);
		writeLimits(datum.getLimits(),out);
	}
	
	/**
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeLimits(Limits datum, OutputStream out) throws IOException{
		if(datum.getMax()<0) {
			out.write(0);
			writeUnsignedLEB128(datum.getMin(), out);
			return;
		}
		out.write(1);
		writeUnsignedLEB128(datum.getMin(), out);
		writeUnsignedLEB128(datum.getMax(), out);
	}

	private static void writeMemoryType(MemoryType datum, OutputStream out) throws IOException{
		writeLimits(datum.getLimits(),out);
	}
	
	private static void writeGlobalType(GlobalType datum, OutputStream out) throws IOException{
		writeValueType(datum.getValueType(), out);
		out.write(datum.getMutability().getMutability());
	}
	
	private static void writeTable(Table datum, OutputStream out) throws IOException{
		writeTableType(datum.getTableType(),out);
	}
	
	private static void writeMemory(Memory datum, OutputStream out) throws IOException{
		writeMemoryType(datum.getMemoryType(), out);
	}
	
	private static void writeGlobal(Global datum, OutputStream out) throws IOException{
		writeGlobalType(datum.getGlobalType(), out);
		writeExpression(datum.getExpression(), out);
	}
	
	/**
	 * https://webassembly.github.io/spec/core/binary/instructions.html#binary-expr
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeExpression(Expression datum, OutputStream out) throws IOException{
		for(Instruction instruction: datum.getInstructions())
			writeInstruction(instruction,out);
		out.write(0x0B);
	}

	private static void writeExport(Export datum, OutputStream out) throws IOException{
		writeName(datum.getName(),out);
		ExportDescriptor exportDescriptor = datum.getExportDescriptor();
		if(exportDescriptor instanceof FunctionIndex) {
			out.write(0);
			writeIndex((FunctionIndex)exportDescriptor, out);
		}
		else if(exportDescriptor instanceof TableIndex) {
			out.write(1);
			writeIndex((TableIndex)exportDescriptor, out);
		}
		else if(exportDescriptor instanceof MemoryIndex) {
			out.write(2);
			writeIndex((MemoryIndex)exportDescriptor, out);
		}
		else if(exportDescriptor instanceof GlobalIndex) {
			out.write(3);
			writeIndex((GlobalIndex)exportDescriptor, out);
		}
		else {
			throw new RuntimeException("ExportDescriptor found of unexpected type, "+exportDescriptor.getClass().getCanonicalName());
		}
	}
	
	private static void writeStart(Start datum, OutputStream out) throws IOException{
		writeIndex(datum.getFunctionIndex(), out);
	}
	
	private static void writeElement(Element datum, OutputStream out) throws IOException{
		boolean isSimpleIndexes = false;
		boolean isActive = datum.getMode()==Element.ElementMode.ACTIVE;
		boolean explicitTableIndex = isActive && (datum.getTable().getIndex()!=0);
		
		int encodingFlag = isActive?0:1;
		if((datum.getMode()==Element.ElementMode.DECLARATIVE)||explicitTableIndex)
			encodingFlag |= 2;
		if(datum.getType() == ReferenceType.REFTYPE_FUNCREF) {
			isSimpleIndexes = true;
			for(Expression expression:datum.getInit()) {
				LinkedList<Instruction> instructions = expression.getInstructions();
				if((instructions.size()!=1)||!(instructions.getFirst() instanceof ReferenceFunction)) {
					isSimpleIndexes = false;
					break;
				}
			}
		}
		if(isSimpleIndexes)
			encodingFlag |= 4;
		out.write(encodingFlag);
		if(explicitTableIndex)
			writeIndex(datum.getTable(), out);
		if(isActive)
			writeExpression(datum.getOffset(), out);
		if(!(isActive&&!explicitTableIndex)) {
			if(isSimpleIndexes)
				out.write(0); // Element Kind
			else
				writeValueType(datum.getType(), out);
		}
		if(isSimpleIndexes) {
			Vector<FunctionIndex> indexes = new Vector<>();
			datum.getInit().stream().forEach((e)->{indexes.add(((ReferenceFunction)e.getInstructions().getFirst()).getFunctionIndex());});
			writeVector(indexes,ModuleSerializer::writeIndex,out);
		}
		else {
			writeVector(datum.getInit(), ModuleSerializer::writeExpression, out);
		}
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeCode(Code datum, OutputStream out) throws IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeVector(datum.getLocals(), ModuleSerializer::writeLocals, bout);
		writeExpression(datum.getExpression(), bout);
		writeUnsignedLEB128(bout.size(), out);
		out.write(bout.toByteArray());
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeLocals(Locals datum, OutputStream out) throws IOException{
		writeUnsignedLEB128(datum.getCount(),out);
		writeValueType(datum.getValueType(), out);
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-data
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeData(Data datum, OutputStream out) throws IOException{
		if(datum.getDataMode() == Data.DataMode.PASSIVE) {
			out.write(1);
		}
		else {
			if(datum.getMemoryIndex().getIndex()==0) {
				out.write(0);
			}
			else {
				out.write(2);
				writeIndex(datum.getMemoryIndex(), out);
			}
			writeExpression(datum.getExpression(), out);
		}
		writeVector(datum.getData(),out);
	}
	
	/********************************************
	 * 
	 * Routines for writing instruction elements
	 * 
	 ********************************************/

	/**
	 * See: https://webassembly.github.io/spec/core/binary/instructions.html#binary-instr
	 * 
	 * @param datum
	 * @param out
	 * @throws IOException
	 */
	private static void writeInstruction(Instruction datum, OutputStream out) throws IOException{
		// Control Instructions
		if(datum instanceof Unreachable) {
			out.write(0);
		}
		else if(datum instanceof Nop) {
			out.write(1);
		}
		else if(datum instanceof Block) {
			out.write(2);
			writeBlockType(((Block)datum).getBlockType(), out);
			for(Instruction instruction:((Block)datum).getInstructions())
				writeInstruction(instruction,out);
			out.write(0x0B);
		}
		else if(datum instanceof Loop) {
			out.write(3);
			writeBlockType(((Loop)datum).getBlockType(), out);
			for(Instruction instruction:((Loop)datum).getInstructions())
				writeInstruction(instruction,out);
			out.write(0x0B);
		}
		else if(datum instanceof If) {
			out.write(4);
			writeBlockType(((If)datum).getBlockType(), out);
			for(Instruction instruction:((If)datum).getIfInstructions())
				writeInstruction(instruction,out);
			if((((If)datum).getElseInstructions()!=null)&&(((If)datum).getElseInstructions().length>0)) {
				out.write(5);
				for(Instruction instruction:((If)datum).getElseInstructions())
					writeInstruction(instruction,out);
			}
			out.write(0x0B);
		}
		else if(datum instanceof Branch) {
			out.write(0x0C);
			writeIndex(((Branch)datum).getLabelIndex(), out);
		}
		else if(datum instanceof BranchIf) {
			out.write(0x0D);
			writeIndex(((BranchIf)datum).getLabelIndex(), out);
		}
		else if(datum instanceof BranchTable) {
			out.write(0x0E);
			writeVector(((BranchTable)datum).getCaseIndices(), ModuleSerializer::writeIndex, out);
			writeIndex(((BranchTable)datum).getDefaultIndex(), out);
		}
		else if(datum instanceof Return) {
			out.write(0x0F);
		}
		else if(datum instanceof AbstractCall) {
			out.write(0x010);
			writeIndex(((AbstractCall)datum).getFunctionIndex(),out);
		}
		else if(datum instanceof CallIndirect) {
			out.write(0x011);
			writeIndex(((CallIndirect)datum).getTypeIndex(),out);
			writeIndex(((CallIndirect)datum).getTableIndex(),out);
		}
		// Reference Instructions
		else if(datum instanceof ReferenceNull) {
			out.write(0x0D0);
			writeValueType(((ReferenceNull)datum).getReferenceType(), out);
		}
		else if(datum instanceof ReferenceIsNull) {
			out.write(0x0D1);
		}
		else if(datum instanceof ReferenceFunction) {
			out.write(0x0D2);
			writeIndex(((ReferenceFunction)datum).getFunctionIndex(), out);
		}
		// Parametric Instructions
		else if(datum instanceof Drop) {
			out.write(0x01A);
		}
		else if(datum instanceof Select) {
			Vector<ValueType> valueTypes = ((Select)datum).getValueTypes();
			if(valueTypes==null||(valueTypes.size()==0)) {
				out.write(0x01B);
			}
			else {
				out.write(0x01C);
				writeVector(valueTypes, ModuleSerializer::writeValueType, out);
			}
		}
		else if(datum instanceof ReferenceFunction) {
			out.write(0x0D2);
			writeIndex(((ReferenceFunction)datum).getFunctionIndex(), out);
		}
		// Variable Instructions
		else if(datum instanceof LocalGet) {
			out.write(0x020);
			writeIndex(((LocalGet)datum).getLocalIndex(), out);
		}
		else if(datum instanceof LocalSet) {
			out.write(0x021);
			writeIndex(((LocalSet)datum).getLocalIndex(), out);
		}
		else if(datum instanceof LocalTee) {
			out.write(0x022);
			writeIndex(((LocalTee)datum).getLocalIndex(), out);
		}
		else if(datum instanceof GlobalGet) {
			out.write(0x023);
			writeIndex(((GlobalGet)datum).getGlobalIndex(), out);
		}
		else if(datum instanceof GlobalSet) {
			out.write(0x024);
			writeIndex(((GlobalSet)datum).getGlobalIndex(), out);
		}
		// Table Instructions
		else if(datum instanceof TableGet) {
			out.write(0x025);
			writeIndex(((TableGet)datum).getTableIndex(), out);
		}
		else if(datum instanceof TableSet) {
			out.write(0x026);
			writeIndex(((TableSet)datum).getTableIndex(), out);
		}
		else if(datum instanceof TableInitialize) {
			out.write(0x0FC);
			writeUnsignedLEB128(12, out);
			writeIndex(((TableInitialize)datum).getElementIndex(), out);
			writeIndex(((TableInitialize)datum).getTableIndex(), out);
		}
		else if(datum instanceof ElementDrop) {
			out.write(0x0FC);
			writeUnsignedLEB128(13, out);
			writeIndex(((TableInitialize)datum).getElementIndex(), out);
		}
		else if(datum instanceof TableCopy) {
			out.write(0x0FC);
			writeUnsignedLEB128(14, out);
			writeIndex(((TableCopy)datum).getTableIndexX(), out);
			writeIndex(((TableCopy)datum).getTableIndexY(), out);
		}
		else if(datum instanceof TableGrow) {
			out.write(0x0FC);
			writeUnsignedLEB128(15, out);
			writeIndex(((TableGrow)datum).getTableIndex(), out);
		}
		else if(datum instanceof TableSize) {
			out.write(0x0FC);
			writeUnsignedLEB128(16, out);
			writeIndex(((TableSize)datum).getTableIndex(), out);
		}
		else if(datum instanceof TableFill) {
			out.write(0x0FC);
			writeUnsignedLEB128(17, out);
			writeIndex(((TableFill)datum).getTableIndex(), out);
		}
		// Memory Instructions
		else if((datum instanceof MemArgInstruction)&&!(datum instanceof VectorMemArgInstruction)) {
			if(datum instanceof I32Load)
				out.write(0x028);
			else if(datum instanceof I64Load)
				out.write(0x029);
			else if(datum instanceof F32Load)
				out.write(0x02A);
			else if(datum instanceof F64Load)
				out.write(0x02B);
			else if(datum instanceof I32Load8S)
				out.write(0x02C);
			else if(datum instanceof I32Load8U)
				out.write(0x02D);
			else if(datum instanceof I32Load16S)
				out.write(0x02E);
			else if(datum instanceof I32Load16U)
				out.write(0x02F);
			else if(datum instanceof I64Load8S)
				out.write(0x030);
			else if(datum instanceof I64Load8U)
				out.write(0x031);
			else if(datum instanceof I64Load16S)
				out.write(0x032);
			else if(datum instanceof I64Load16U)
				out.write(0x033);
			else if(datum instanceof I64Load32S)
				out.write(0x034);
			else if(datum instanceof I64Load32U)
				out.write(0x035);
			else if(datum instanceof I32Store)
				out.write(0x036);
			else if(datum instanceof I64Store)
				out.write(0x037);
			else if(datum instanceof F32Store)
				out.write(0x038);
			else if(datum instanceof F64Store)
				out.write(0x039);
			else if(datum instanceof I32Store8)
				out.write(0x03A);
			else if(datum instanceof I32Store16)
				out.write(0x03B);
			else if(datum instanceof I64Store8)
				out.write(0x03C);
			else if(datum instanceof I64Store16)
				out.write(0x03D);
			else if(datum instanceof I64Store32)
				out.write(0x03E);
			else
				throw new RuntimeException("Unrecognized MemArg instruction type: "+datum.getClass().getCanonicalName());
			writeMemArgInstruction((MemArgInstruction)datum, out);
		}
		else if(datum instanceof MemorySize) {
			out.write(0x03F);
			out.write(0);
		}
		else if(datum instanceof MemoryGrow) {
			out.write(0x040);
			out.write(0);
		}
		else if(datum instanceof MemoryInitialize) {
			out.write(0x0FC);
			writeUnsignedLEB128(8, out);
			writeIndex(((MemoryInitialize)datum).getDataIndex(), out);
			out.write(0);
		}
		else if(datum instanceof DataDrop) {
			out.write(0x0FC);
			writeUnsignedLEB128(9, out);
			writeIndex(((MemoryInitialize)datum).getDataIndex(), out);
		}
		else if(datum instanceof MemoryCopy) {
			out.write(0x0FC);
			writeUnsignedLEB128(10, out);
			out.write(0);
			out.write(0);
		}
		else if(datum instanceof MemoryFill) {
			out.write(0x0FC);
			writeUnsignedLEB128(11, out);
			out.write(0);
		}
		// Numeric Instructions
		else if(datum instanceof I32Const) {
			out.write(0x041);
			writeSignedLEB128(((I32Const)datum).getValue(), out);
		}
		else if(datum instanceof I64Const) {
			out.write(0x042);
			writeSignedLEB128(((I64Const)datum).getValue(), out);
		}
		else if(datum instanceof F32Const) {
			out.write(0x043);
			writeFloat(((F32Const)datum).getValue(), out);;
		}
		else if(datum instanceof F64Const) {
			out.write(0x044);
			writeDouble(((F64Const)datum).getValue(), out);;
		}
		else if(datum instanceof I32Eqz) {
			out.write(0x045);
		}
		else if(datum instanceof I32Eq) {
			out.write(0x046);
		}
		else if(datum instanceof I32Ne) {
			out.write(0x047);
		}
		else if(datum instanceof I32LtS) {
			out.write(0x048);
		}
		else if(datum instanceof I32LtU) {
			out.write(0x049);
		}
		else if(datum instanceof I32GtS) {
			out.write(0x04A);
		}
		else if(datum instanceof I32GtU) {
			out.write(0x04B);
		}
		else if(datum instanceof I32LeS) {
			out.write(0x04C);
		}
		else if(datum instanceof I32LeU) {
			out.write(0x04D);
		}
		else if(datum instanceof I32GeS) {
			out.write(0x04E);
		}
		else if(datum instanceof I32GeU) {
			out.write(0x04F);
		}
		else if(datum instanceof I64Eqz) {
			out.write(0x050);
		}
		else if(datum instanceof I64Eq) {
			out.write(0x051);
		}
		else if(datum instanceof I64Ne) {
			out.write(0x052);
		}
		else if(datum instanceof I64LtS) {
			out.write(0x053);
		}
		else if(datum instanceof I64LtU) {
			out.write(0x054);
		}
		else if(datum instanceof I64GtS) {
			out.write(0x055);
		}
		else if(datum instanceof I64GtU) {
			out.write(0x056);
		}
		else if(datum instanceof I64LeS) {
			out.write(0x057);
		}
		else if(datum instanceof I64LeU) {
			out.write(0x058);
		}
		else if(datum instanceof I64GeS) {
			out.write(0x059);
		}
		else if(datum instanceof I64GeU) {
			out.write(0x05A);
		}
		else if(datum instanceof F32Eq) {
			out.write(0x05B);
		}
		else if(datum instanceof F32Ne) {
			out.write(0x05C);
		}
		else if(datum instanceof F32Lt) {
			out.write(0x05D);
		}
		else if(datum instanceof F32Gt) {
			out.write(0x05E);
		}
		else if(datum instanceof F32Le) {
			out.write(0x05F);
		}
		else if(datum instanceof F32Ge) {
			out.write(0x060);
		}
		else if(datum instanceof F64Eq) {
			out.write(0x061);
		}
		else if(datum instanceof F64Ne) {
			out.write(0x062);
		}
		else if(datum instanceof F64Lt) {
			out.write(0x063);
		}
		else if(datum instanceof F64Gt) {
			out.write(0x064);
		}
		else if(datum instanceof F64Le) {
			out.write(0x065);
		}
		else if(datum instanceof F64Ge) {
			out.write(0x066);
		}
		else if(datum instanceof I32Clz) {
			out.write(0x067);
		}
		else if(datum instanceof I32Ctz) {
			out.write(0x068);
		}
		else if(datum instanceof I32Popcnt) {
			out.write(0x069);
		}
		else if(datum instanceof I32Add) {
			out.write(0x06A);
		}
		else if(datum instanceof I32Sub) {
			out.write(0x06B);
		}
		else if(datum instanceof I32Mul) {
			out.write(0x06C);
		}
		else if(datum instanceof I32DivS) {
			out.write(0x06D);
		}
		else if(datum instanceof I32DivU) {
			out.write(0x06E);
		}
		else if(datum instanceof I32RemS) {
			out.write(0x06F);
		}
		else if(datum instanceof I32RemU) {
			out.write(0x070);
		}
		else if(datum instanceof I32And) {
			out.write(0x071);
		}
		else if(datum instanceof I32Or) {
			out.write(0x072);
		}
		else if(datum instanceof I32Xor) {
			out.write(0x073);
		}
		else if(datum instanceof I32Shl) {
			out.write(0x074);
		}
		else if(datum instanceof I32ShrS) {
			out.write(0x075);
		}
		else if(datum instanceof I32ShrU) {
			out.write(0x076);
		}
		else if(datum instanceof I32Rotl) {
			out.write(0x077);
		}
		else if(datum instanceof I32Rotr) {
			out.write(0x078);
		}
		else if(datum instanceof I64Clz) {
			out.write(0x079);
		}
		else if(datum instanceof I64Ctz) {
			out.write(0x07A);
		}
		else if(datum instanceof I64Popcnt) {
			out.write(0x07B);
		}
		else if(datum instanceof I64Add) {
			out.write(0x07C);
		}
		else if(datum instanceof I64Sub) {
			out.write(0x07D);
		}
		else if(datum instanceof I64Mul) {
			out.write(0x07E);
		}
		else if(datum instanceof I64DivS) {
			out.write(0x07F);
		}
		else if(datum instanceof I64DivU) {
			out.write(0x080);
		}
		else if(datum instanceof I64RemS) {
			out.write(0x081);
		}
		else if(datum instanceof I64RemU) {
			out.write(0x082);
		}
		else if(datum instanceof I64And) {
			out.write(0x083);
		}
		else if(datum instanceof I64Or) {
			out.write(0x084);
		}
		else if(datum instanceof I64Xor) {
			out.write(0x085);
		}
		else if(datum instanceof I64Shl) {
			out.write(0x086);
		}
		else if(datum instanceof I64ShrS) {
			out.write(0x087);
		}
		else if(datum instanceof I64ShrU) {
			out.write(0x088);
		}
		else if(datum instanceof I64Rotl) {
			out.write(0x089);
		}
		else if(datum instanceof I64Rotr) {
			out.write(0x08A);
		}
		else if(datum instanceof F32Abs) {
			out.write(0x08B);
		}
		else if(datum instanceof F32Neg) {
			out.write(0x08C);
		}
		else if(datum instanceof F32Ceil) {
			out.write(0x08D);
		}
		else if(datum instanceof F32Floor) {
			out.write(0x08E);
		}
		else if(datum instanceof F32Trunc) {
			out.write(0x08F);
		}
		else if(datum instanceof F32Nearest) {
			out.write(0x090);
		}
		else if(datum instanceof F32Sqrt) {
			out.write(0x091);
		}
		else if(datum instanceof F32Add) {
			out.write(0x092);
		}
		else if(datum instanceof F32Sub) {
			out.write(0x093);
		}
		else if(datum instanceof F32Mul) {
			out.write(0x094);
		}
		else if(datum instanceof F32Div) {
			out.write(0x095);
		}
		else if(datum instanceof F32Min) {
			out.write(0x096);
		}
		else if(datum instanceof F32Max) {
			out.write(0x097);
		}
		else if(datum instanceof F32CopySign) {
			out.write(0x098);
		}
		else if(datum instanceof F64Abs) {
			out.write(0x099);
		}
		else if(datum instanceof F64Neg) {
			out.write(0x09A);
		}
		else if(datum instanceof F64Ceil) {
			out.write(0x09B);
		}
		else if(datum instanceof F64Floor) {
			out.write(0x09C);
		}
		else if(datum instanceof F64Trunc) {
			out.write(0x09D);
		}
		else if(datum instanceof F64Nearest) {
			out.write(0x09E);
		}
		else if(datum instanceof F64Sqrt) {
			out.write(0x09F);
		}
		else if(datum instanceof F64Add) {
			out.write(0x0A0);
		}
		else if(datum instanceof F64Sub) {
			out.write(0x0A1);
		}
		else if(datum instanceof F64Mul) {
			out.write(0x0A2);
		}
		else if(datum instanceof F64Div) {
			out.write(0x0A3);
		}
		else if(datum instanceof F64Min) {
			out.write(0x0A4);
		}
		else if(datum instanceof F64Max) {
			out.write(0x0A5);
		}
		else if(datum instanceof F64CopySign) {
			out.write(0x0A6);
		}
		else if(datum instanceof I32WrapI64) {
			out.write(0x0A7);
		}
		else if(datum instanceof I32TruncF32S) {
			out.write(0x0A8);
		}
		else if(datum instanceof I32TruncF32U) {
			out.write(0x0A9);
		}
		else if(datum instanceof I32TruncF64S) {
			out.write(0x0AA);
		}
		else if(datum instanceof I32TruncF64U) {
			out.write(0x0AB);
		}
		else if(datum instanceof I64ExtendI32S) {
			out.write(0x0AC);
		}
		else if(datum instanceof I64ExtendI32U) {
			out.write(0x0AD);
		}
		else if(datum instanceof I64TruncF32S) {
			out.write(0x0AE);
		}
		else if(datum instanceof I64TruncF32U) {
			out.write(0x0AF);
		}
		else if(datum instanceof I64TruncF64S) {
			out.write(0x0B0);
		}
		else if(datum instanceof I64TruncF64U) {
			out.write(0x0B1);
		}
		else if(datum instanceof F32ConvertI32S) {
			out.write(0x0B2);
		}
		else if(datum instanceof F32ConvertI32U) {
			out.write(0x0B3);
		}
		else if(datum instanceof F32ConvertI64S) {
			out.write(0x0B4);
		}
		else if(datum instanceof F32ConvertI64U) {
			out.write(0x0B5);
		}
		else if(datum instanceof F32DemoteF64) {
			out.write(0x0B6);
		}
		else if(datum instanceof F64ConvertI32S) {
			out.write(0x0B7);
		}
		else if(datum instanceof F64ConvertI32U) {
			out.write(0x0B8);
		}
		else if(datum instanceof F64ConvertI64S) {
			out.write(0x0B9);
		}
		else if(datum instanceof F64ConvertI64U) {
			out.write(0x0BA);
		}
		else if(datum instanceof F64PromoteF32) {
			out.write(0x0BB);
		}
		else if(datum instanceof I32ReinterpretF32) {
			out.write(0x0BC);
		}
		else if(datum instanceof I64ReinterpretF64) {
			out.write(0x0BD);
		}
		else if(datum instanceof F32ReinterpretI32) {
			out.write(0x0BE);
		}
		else if(datum instanceof F64ReinterpretI64) {
			out.write(0x0BF);
		}
		else if(datum instanceof I32Extend8S) {
			out.write(0x0C0);
		}
		else if(datum instanceof I32Extend16S) {
			out.write(0x0C1);
		}
		else if(datum instanceof I64Extend8S) {
			out.write(0x0C2);
		}
		else if(datum instanceof I64Extend16S) {
			out.write(0x0C3);
		}
		else if(datum instanceof I64Extend32S) {
			out.write(0x0C4);
		}
		else if(datum instanceof I32TruncSatF32S) {
			out.write(0x0FC);
			writeUnsignedLEB128(0, out);
		}
		else if(datum instanceof I32TruncSatF32U) {
			out.write(0x0FC);
			writeUnsignedLEB128(1, out);
		}
		else if(datum instanceof I32TruncSatF64S) {
			out.write(0x0FC);
			writeUnsignedLEB128(2, out);
		}
		else if(datum instanceof I32TruncSatF64U) {
			out.write(0x0FC);
			writeUnsignedLEB128(3, out);
		}
		else if(datum instanceof I64TruncSatF32S) {
			out.write(0x0FC);
			writeUnsignedLEB128(4, out);
		}
		else if(datum instanceof I64TruncSatF32U) {
			out.write(0x0FC);
			writeUnsignedLEB128(5, out);
		}
		else if(datum instanceof I64TruncSatF64S) {
			out.write(0x0FC);
			writeUnsignedLEB128(6, out);
		}
		else if(datum instanceof I64TruncSatF64U) {
			out.write(0x0FC);
			writeUnsignedLEB128(7, out);
		}
		// Vector Instructions
		else if(datum instanceof VectorMemArgInstruction) {
			out.write(0x0FD);
			if(datum instanceof V128Load)
				writeUnsignedLEB128(0, out);
			else if(datum instanceof V128Load8x8S)
				writeUnsignedLEB128(1, out);
			else if(datum instanceof V128Load8x8U)
				writeUnsignedLEB128(2, out);
			else if(datum instanceof V128Load16x4S)
				writeUnsignedLEB128(3, out);
			else if(datum instanceof V128Load16x4U)
				writeUnsignedLEB128(4, out);
			else if(datum instanceof V128Load32x2S)
				writeUnsignedLEB128(5, out);
			else if(datum instanceof V128Load32x2U)
				writeUnsignedLEB128(6, out);
			else if(datum instanceof V128Load8Splat)
				writeUnsignedLEB128(7, out);
			else if(datum instanceof V128Load16Splat)
				writeUnsignedLEB128(8, out);
			else if(datum instanceof V128Load32Splat)
				writeUnsignedLEB128(9, out);
			else if(datum instanceof V128Load64Splat)
				writeUnsignedLEB128(10, out);
			else if(datum instanceof V128Load32Zero)
				writeUnsignedLEB128(92, out);
			else if(datum instanceof V128Load64Zero)
				writeUnsignedLEB128(93, out);
			else if(datum instanceof V128Store)
				writeUnsignedLEB128(11, out);
			else if(datum instanceof V128Load8Lane)
				writeUnsignedLEB128(84, out);
			else if(datum instanceof V128Load16Lane)
				writeUnsignedLEB128(85, out);
			else if(datum instanceof V128Load32Lane)
				writeUnsignedLEB128(86, out);
			else if(datum instanceof V128Load64Lane)
				writeUnsignedLEB128(87, out);
			else if(datum instanceof V128Store8Lane)
				writeUnsignedLEB128(88, out);
			else if(datum instanceof V128Store16Lane)
				writeUnsignedLEB128(89, out);
			else if(datum instanceof V128Store32Lane)
				writeUnsignedLEB128(90, out);
			else if(datum instanceof V128Store64Lane)
				writeUnsignedLEB128(91, out);
			else
				throw new RuntimeException("Unrecognized VectorMemArgInstruction type: "+datum.getClass().getCanonicalName());
			writeMemArgInstruction((MemArgInstruction)datum, out);
			if(datum instanceof LaneInstruction)
				out.write(((LaneInstruction)datum).getLaneIndex());
		}
		else if(datum instanceof V128Const) {
			out.write(0x0FD);
			writeUnsignedLEB128(12, out);
			out.write(((V128Const)datum).getValue());
		}
		else if(datum instanceof I8x16Shuffle) {
			out.write(0x0FD);
			writeUnsignedLEB128(13, out);
			out.write(((I8x16Shuffle)datum).getLaneIndexes());
		}
		else if(datum instanceof LaneInstruction) {
			out.write(0x0FD);
			if(datum instanceof I8x16ExtractLaneS)
				writeUnsignedLEB128(21, out);
			else if(datum instanceof I8x16ExtractLaneU)
				writeUnsignedLEB128(22, out);
			else if(datum instanceof I8x16ReplaceLane)
				writeUnsignedLEB128(23, out);
			else if(datum instanceof I16x8ExtractLaneS)
				writeUnsignedLEB128(24, out);
			else if(datum instanceof I16x8ExtractLaneU)
				writeUnsignedLEB128(25, out);
			else if(datum instanceof I16x8ReplaceLane)
				writeUnsignedLEB128(26, out);
			else if(datum instanceof I32x4ExtractLane)
				writeUnsignedLEB128(27, out);
			else if(datum instanceof I32x4ReplaceLane)
				writeUnsignedLEB128(28, out);
			else if(datum instanceof I64x2ExtractLane)
				writeUnsignedLEB128(29, out);
			else if(datum instanceof I64x2ReplaceLane)
				writeUnsignedLEB128(30, out);
			else if(datum instanceof F32x4ExtractLane)
				writeUnsignedLEB128(31, out);
			else if(datum instanceof F32x4ReplaceLane)
				writeUnsignedLEB128(32, out);
			else if(datum instanceof F64x2ExtractLane)
				writeUnsignedLEB128(33, out);
			else if(datum instanceof F64x2ReplaceLane)
				writeUnsignedLEB128(34, out);
			else
				throw new RuntimeException("Unrecognized LaneInstruction type: "+datum.getClass().getCanonicalName());
			out.write(((LaneInstruction)datum).getLaneIndex());
		}
		else if(datum instanceof I8x16Swizzle) {
			out.write(0x0FD);
			writeUnsignedLEB128(14, out);
		}
		else if(datum instanceof I8x16Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(15, out);
		}
		else if(datum instanceof I16x8Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(16, out);
		}
		else if(datum instanceof I32x4Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(17, out);
		}
		else if(datum instanceof I64x2Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(18, out);
		}
		else if(datum instanceof F32x4Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(19, out);
		}
		else if(datum instanceof F64x2Splat) {
			out.write(0x0FD);
			writeUnsignedLEB128(20, out);
		}
		else if(datum instanceof I8x16Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(35, out);
		}
		else if(datum instanceof I8x16Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(36, out);
		}
		else if(datum instanceof I8x16LtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(37, out);
		}
		else if(datum instanceof I8x16LtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(38, out);
		}
		else if(datum instanceof I8x16GtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(39, out);
		}
		else if(datum instanceof I8x16GtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(40, out);
		}
		else if(datum instanceof I8x16LeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(41, out);
		}
		else if(datum instanceof I8x16LeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(42, out);
		}
		else if(datum instanceof I8x16GeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(43, out);
		}
		else if(datum instanceof I8x16GeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(44, out);
		}
		else if(datum instanceof I16x8Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(45, out);
		}
		else if(datum instanceof I16x8Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(46, out);
		}
		else if(datum instanceof I16x8LtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(47, out);
		}
		else if(datum instanceof I16x8LtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(48, out);
		}
		else if(datum instanceof I16x8GtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(49, out);
		}
		else if(datum instanceof I16x8GtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(50, out);
		}
		else if(datum instanceof I16x8LeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(51, out);
		}
		else if(datum instanceof I16x8LeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(52, out);
		}
		else if(datum instanceof I16x8GeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(53, out);
		}
		else if(datum instanceof I16x8GeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(54, out);
		}
		else if(datum instanceof I32x4Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(55, out);
		}
		else if(datum instanceof I32x4Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(56, out);
		}
		else if(datum instanceof I32x4LtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(57, out);
		}
		else if(datum instanceof I32x4LtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(58, out);
		}
		else if(datum instanceof I32x4GtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(59, out);
		}
		else if(datum instanceof I32x4GtU) {
			out.write(0x0FD);
			writeUnsignedLEB128(60, out);
		}
		else if(datum instanceof I32x4LeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(61, out);
		}
		else if(datum instanceof I32x4LeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(62, out);
		}
		else if(datum instanceof I32x4GeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(63, out);
		}
		else if(datum instanceof I32x4GeU) {
			out.write(0x0FD);
			writeUnsignedLEB128(64, out);
		}
		else if(datum instanceof I64x2Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(214, out);
		}
		else if(datum instanceof I64x2Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(215, out);
		}
		else if(datum instanceof I64x2LtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(216, out);
		}
		else if(datum instanceof I64x2GtS) {
			out.write(0x0FD);
			writeUnsignedLEB128(217, out);
		}
		else if(datum instanceof I64x2LeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(218, out);
		}
		else if(datum instanceof I64x2GeS) {
			out.write(0x0FD);
			writeUnsignedLEB128(219, out);
		}
		else if(datum instanceof F32x4Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(65, out);
		}
		else if(datum instanceof F32x4Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(66, out);
		}
		else if(datum instanceof F32x4Lt) {
			out.write(0x0FD);
			writeUnsignedLEB128(67, out);
		}
		else if(datum instanceof F32x4Gt) {
			out.write(0x0FD);
			writeUnsignedLEB128(68, out);
		}
		else if(datum instanceof F32x4Le) {
			out.write(0x0FD);
			writeUnsignedLEB128(69, out);
		}
		else if(datum instanceof F32x4Ge) {
			out.write(0x0FD);
			writeUnsignedLEB128(70, out);
		}
		else if(datum instanceof F64x2Eq) {
			out.write(0x0FD);
			writeUnsignedLEB128(71, out);
		}
		else if(datum instanceof F64x2Ne) {
			out.write(0x0FD);
			writeUnsignedLEB128(72, out);
		}
		else if(datum instanceof F64x2Lt) {
			out.write(0x0FD);
			writeUnsignedLEB128(73, out);
		}
		else if(datum instanceof F64x2Gt) {
			out.write(0x0FD);
			writeUnsignedLEB128(74, out);
		}
		else if(datum instanceof F64x2Le) {
			out.write(0x0FD);
			writeUnsignedLEB128(75, out);
		}
		else if(datum instanceof F64x2Ge) {
			out.write(0x0FD);
			writeUnsignedLEB128(76, out);
		}
		else if(datum instanceof V128Not) {
			out.write(0x0FD);
			writeUnsignedLEB128(77, out);
		}
		else if(datum instanceof V128And) {
			out.write(0x0FD);
			writeUnsignedLEB128(78, out);
		}
		else if(datum instanceof V128AndNot) {
			out.write(0x0FD);
			writeUnsignedLEB128(79, out);
		}
		else if(datum instanceof V128Or) {
			out.write(0x0FD);
			writeUnsignedLEB128(80, out);
		}
		else if(datum instanceof V128Xor) {
			out.write(0x0FD);
			writeUnsignedLEB128(81, out);
		}
		else if(datum instanceof V128BitSelect) {
			out.write(0x0FD);
			writeUnsignedLEB128(82, out);
		}
		else if(datum instanceof V128AnyTrue) {
			out.write(0x0FD);
			writeUnsignedLEB128(83, out);
		}
		else if(datum instanceof I8x16Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(96, out);
		}
		else if(datum instanceof I8x16Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(97, out);
		}
		else if(datum instanceof I8x16PopCnt) {
			out.write(0x0FD);
			writeUnsignedLEB128(98, out);
		}
		else if(datum instanceof I8x16AllTrue) {
			out.write(0x0FD);
			writeUnsignedLEB128(99, out);
		}
		else if(datum instanceof I8x16BitMask) {
			out.write(0x0FD);
			writeUnsignedLEB128(100, out);
		}
		else if(datum instanceof I8x16NarrowI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(101, out);
		}
		else if(datum instanceof I8x16NarrowI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(102, out);
		}
		else if(datum instanceof I8x16Shl) {
			out.write(0x0FD);
			writeUnsignedLEB128(107, out);
		}
		else if(datum instanceof I8x16ShrS) {
			out.write(0x0FD);
			writeUnsignedLEB128(108, out);
		}
		else if(datum instanceof I8x16ShrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(109, out);
		}
		else if(datum instanceof I8x16Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(110, out);
		}
		else if(datum instanceof I8x16AddSatS) {
			out.write(0x0FD);
			writeUnsignedLEB128(111, out);
		}
		else if(datum instanceof I8x16AddSatU) {
			out.write(0x0FD);
			writeUnsignedLEB128(112, out);
		}
		else if(datum instanceof I8x16Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(113, out);
		}
		else if(datum instanceof I8x16SubSatS) {
			out.write(0x0FD);
			writeUnsignedLEB128(114, out);
		}
		else if(datum instanceof I8x16SubSatU) {
			out.write(0x0FD);
			writeUnsignedLEB128(115, out);
		}
		else if(datum instanceof I8x16MinS) {
			out.write(0x0FD);
			writeUnsignedLEB128(118, out);
		}
		else if(datum instanceof I8x16MinU) {
			out.write(0x0FD);
			writeUnsignedLEB128(119, out);
		}
		else if(datum instanceof I8x16MaxS) {
			out.write(0x0FD);
			writeUnsignedLEB128(120, out);
		}
		else if(datum instanceof I8x16MaxU) {
			out.write(0x0FD);
			writeUnsignedLEB128(121, out);
		}
		else if(datum instanceof I8x16AvgrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(123, out);
		}
		else if(datum instanceof I16x8ExtaddPairwiseI8x16S) {
			out.write(0x0FD);
			writeUnsignedLEB128(124, out);
		}
		else if(datum instanceof I16x8ExtaddPairwiseI8x16U) {
			out.write(0x0FD);
			writeUnsignedLEB128(125, out);
		}
		else if(datum instanceof I16x8Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(128, out);
		}
		else if(datum instanceof I16x8Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(129, out);
		}
		else if(datum instanceof I16x8Q15MulrSatS) {
			out.write(0x0FD);
			writeUnsignedLEB128(130, out);
		}
		else if(datum instanceof I16x8AllTrue) {
			out.write(0x0FD);
			writeUnsignedLEB128(131, out);
		}
		else if(datum instanceof I16x8BitMask) {
			out.write(0x0FD);
			writeUnsignedLEB128(132, out);
		}
		else if(datum instanceof I16x8NarrowI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(133, out);
		}
		else if(datum instanceof I16x8NarrowI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(134, out);
		}
		else if(datum instanceof I16x8ExtendLowI8x16S) {
			out.write(0x0FD);
			writeUnsignedLEB128(135, out);
		}
		else if(datum instanceof I16x8ExtendHighI8x16S) {
			out.write(0x0FD);
			writeUnsignedLEB128(136, out);
		}
		else if(datum instanceof I16x8ExtendLowI8x16U) {
			out.write(0x0FD);
			writeUnsignedLEB128(137, out);
		}
		else if(datum instanceof I16x8ExtendHighI8x16U) {
			out.write(0x0FD);
			writeUnsignedLEB128(138, out);
		}
		else if(datum instanceof I16x8Shl) {
			out.write(0x0FD);
			writeUnsignedLEB128(139, out);
		}
		else if(datum instanceof I16x8ShrS) {
			out.write(0x0FD);
			writeUnsignedLEB128(140, out);
		}
		else if(datum instanceof I16x8ShrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(141, out);
		}
		else if(datum instanceof I16x8Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(142, out);
		}
		else if(datum instanceof I16x8AddSatS) {
			out.write(0x0FD);
			writeUnsignedLEB128(143, out);
		}
		else if(datum instanceof I16x8AddSatU) {
			out.write(0x0FD);
			writeUnsignedLEB128(144, out);
		}
		else if(datum instanceof I16x8Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(145, out);
		}
		else if(datum instanceof I16x8SubSatS) {
			out.write(0x0FD);
			writeUnsignedLEB128(146, out);
		}
		else if(datum instanceof I16x8SubSatU) {
			out.write(0x0FD);
			writeUnsignedLEB128(147, out);
		}
		else if(datum instanceof I16x8Mul) {
			out.write(0x0FD);
			writeUnsignedLEB128(149, out);
		}
		else if(datum instanceof I16x8MinS) {
			out.write(0x0FD);
			writeUnsignedLEB128(150, out);
		}
		else if(datum instanceof I16x8MinU) {
			out.write(0x0FD);
			writeUnsignedLEB128(151, out);
		}
		else if(datum instanceof I16x8MaxS) {
			out.write(0x0FD);
			writeUnsignedLEB128(152, out);
		}
		else if(datum instanceof I16x8MaxU) {
			out.write(0x0FD);
			writeUnsignedLEB128(153, out);
		}
		else if(datum instanceof I16x8AvgrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(155, out);
		}
		else if(datum instanceof I16x8ExtmulLowI8x16S) {
			out.write(0x0FD);
			writeUnsignedLEB128(156, out);
		}
		else if(datum instanceof I16x8ExtmulHighI8x16S) {
			out.write(0x0FD);
			writeUnsignedLEB128(157, out);
		}
		else if(datum instanceof I16x8ExtmulLowI8x16U) {
			out.write(0x0FD);
			writeUnsignedLEB128(158, out);
		}
		else if(datum instanceof I16x8ExtmulHighI8x16U) {
			out.write(0x0FD);
			writeUnsignedLEB128(159, out);
		}
		else if(datum instanceof I32x4ExtaddPairwiseI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(126, out);
		}
		else if(datum instanceof I32x4ExtaddPairwiseI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(127, out);
		}
		else if(datum instanceof I32x4Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(160, out);
		}
		else if(datum instanceof I32x4Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(161, out);
		}
		else if(datum instanceof I32x4AllTrue) {
			out.write(0x0FD);
			writeUnsignedLEB128(162, out);
		}
		else if(datum instanceof I32x4BitMask) {
			out.write(0x0FD);
			writeUnsignedLEB128(163, out);
		}
		else if(datum instanceof I32x4ExtendLowI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(167, out);
		}
		else if(datum instanceof I32x4ExtendHighI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(168, out);
		}
		else if(datum instanceof I32x4ExtendLowI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(169, out);
		}
		else if(datum instanceof I32x4ExtendHighI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(170, out);
		}
		else if(datum instanceof I32x4Shl) {
			out.write(0x0FD);
			writeUnsignedLEB128(171, out);
		}
		else if(datum instanceof I32x4ShrS) {
			out.write(0x0FD);
			writeUnsignedLEB128(172, out);
		}
		else if(datum instanceof I32x4ShrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(173, out);
		}
		else if(datum instanceof I32x4Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(174, out);
		}
		else if(datum instanceof I32x4Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(177, out);
		}
		else if(datum instanceof I32x4Mul) {
			out.write(0x0FD);
			writeUnsignedLEB128(181, out);
		}
		else if(datum instanceof I32x4MinS) {
			out.write(0x0FD);
			writeUnsignedLEB128(182, out);
		}
		else if(datum instanceof I32x4MinU) {
			out.write(0x0FD);
			writeUnsignedLEB128(183, out);
		}
		else if(datum instanceof I32x4MaxS) {
			out.write(0x0FD);
			writeUnsignedLEB128(184, out);
		}
		else if(datum instanceof I32x4MaxU) {
			out.write(0x0FD);
			writeUnsignedLEB128(185, out);
		}
		else if(datum instanceof I32x4DotI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(186, out);
		}
		else if(datum instanceof I32x4ExtmulLowI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(188, out);
		}
		else if(datum instanceof I32x4ExtmulHighI16x8S) {
			out.write(0x0FD);
			writeUnsignedLEB128(189, out);
		}
		else if(datum instanceof I32x4ExtmulLowI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(190, out);
		}
		else if(datum instanceof I32x4ExtmulHighI16x8U) {
			out.write(0x0FD);
			writeUnsignedLEB128(191, out);
		}
		else if(datum instanceof I64x2Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(192, out);
		}
		else if(datum instanceof I64x2Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(193, out);
		}
		else if(datum instanceof I64x2AllTrue) {
			out.write(0x0FD);
			writeUnsignedLEB128(195, out);
		}
		else if(datum instanceof I64x2BitMask) {
			out.write(0x0FD);
			writeUnsignedLEB128(196, out);
		}
		else if(datum instanceof I64x2ExtendLowI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(199, out);
		}
		else if(datum instanceof I64x2ExtendHighI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(200, out);
		}
		else if(datum instanceof I64x2ExtendLowI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(201, out);
		}
		else if(datum instanceof I64x2ExtendHighI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(202, out);
		}
		else if(datum instanceof I64x2Shl) {
			out.write(0x0FD);
			writeUnsignedLEB128(203, out);
		}
		else if(datum instanceof I64x2ShrS) {
			out.write(0x0FD);
			writeUnsignedLEB128(204, out);
		}
		else if(datum instanceof I64x2ShrU) {
			out.write(0x0FD);
			writeUnsignedLEB128(205, out);
		}
		else if(datum instanceof I64x2Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(206, out);
		}
		else if(datum instanceof I64x2Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(209, out);
		}
		else if(datum instanceof I64x2Mul) {
			out.write(0x0FD);
			writeUnsignedLEB128(213, out);
		}
		else if(datum instanceof I64x2ExtmulLowI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(220, out);
		}
		else if(datum instanceof I64x2ExtmulHighI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(221, out);
		}
		else if(datum instanceof I64x2ExtmulLowI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(222, out);
		}
		else if(datum instanceof I64x2ExtmulHighI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(223, out);
		}
		else if(datum instanceof F32x4Ceil) {
			out.write(0x0FD);
			writeUnsignedLEB128(103, out);
		}
		else if(datum instanceof F32x4Floor) {
			out.write(0x0FD);
			writeUnsignedLEB128(104, out);
		}
		else if(datum instanceof F32x4Trunc) {
			out.write(0x0FD);
			writeUnsignedLEB128(105, out);
		}
		else if(datum instanceof F32x4Nearest) {
			out.write(0x0FD);
			writeUnsignedLEB128(106, out);
		}
		else if(datum instanceof F32x4Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(224, out);
		}
		else if(datum instanceof F32x4Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(225, out);
		}
		else if(datum instanceof F32x4Sqrt) {
			out.write(0x0FD);
			writeUnsignedLEB128(227, out);
		}
		else if(datum instanceof F32x4Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(228, out);
		}
		else if(datum instanceof F32x4Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(229, out);
		}
		else if(datum instanceof F32x4Mul) {
			out.write(0x0FD);
			writeUnsignedLEB128(230, out);
		}
		else if(datum instanceof F32x4Div) {
			out.write(0x0FD);
			writeUnsignedLEB128(231, out);
		}
		else if(datum instanceof F32x4Min) {
			out.write(0x0FD);
			writeUnsignedLEB128(232, out);
		}
		else if(datum instanceof F32x4Max) {
			out.write(0x0FD);
			writeUnsignedLEB128(233, out);
		}
		else if(datum instanceof F32x4PMin) {
			out.write(0x0FD);
			writeUnsignedLEB128(234, out);
		}
		else if(datum instanceof F32x4PMax) {
			out.write(0x0FD);
			writeUnsignedLEB128(235, out);
		}
		else if(datum instanceof F64x2Ceil) {
			out.write(0x0FD);
			writeUnsignedLEB128(116, out);
		}
		else if(datum instanceof F64x2Floor) {
			out.write(0x0FD);
			writeUnsignedLEB128(117, out);
		}
		else if(datum instanceof F64x2Trunc) {
			out.write(0x0FD);
			writeUnsignedLEB128(122, out);
		}
		else if(datum instanceof F64x2Nearest) {
			out.write(0x0FD);
			writeUnsignedLEB128(148, out);
		}
		else if(datum instanceof F64x2Abs) {
			out.write(0x0FD);
			writeUnsignedLEB128(236, out);
		}
		else if(datum instanceof F64x2Neg) {
			out.write(0x0FD);
			writeUnsignedLEB128(237, out);
		}
		else if(datum instanceof F64x2Sqrt) {
			out.write(0x0FD);
			writeUnsignedLEB128(239, out);
		}
		else if(datum instanceof F64x2Add) {
			out.write(0x0FD);
			writeUnsignedLEB128(240, out);
		}
		else if(datum instanceof F64x2Sub) {
			out.write(0x0FD);
			writeUnsignedLEB128(241, out);
		}
		else if(datum instanceof F64x2Mul) {
			out.write(0x0FD);
			writeUnsignedLEB128(242, out);
		}
		else if(datum instanceof F64x2Div) {
			out.write(0x0FD);
			writeUnsignedLEB128(243, out);
		}
		else if(datum instanceof F64x2Min) {
			out.write(0x0FD);
			writeUnsignedLEB128(244, out);
		}
		else if(datum instanceof F64x2Max) {
			out.write(0x0FD);
			writeUnsignedLEB128(245, out);
		}
		else if(datum instanceof F64x2PMin) {
			out.write(0x0FD);
			writeUnsignedLEB128(246, out);
		}
		else if(datum instanceof F64x2PMax) {
			out.write(0x0FD);
			writeUnsignedLEB128(247, out);
		}
		else if(datum instanceof I32x4TruncSatF32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(248, out);
		}
		else if(datum instanceof I32x4TruncSatF32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(249, out);
		}
		else if(datum instanceof F32x4ConvertI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(250, out);
		}
		else if(datum instanceof F32x4ConvertI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(251, out);
		}
		else if(datum instanceof I32x4TruncSatF64x2SZero) {
			out.write(0x0FD);
			writeUnsignedLEB128(252, out);
		}
		else if(datum instanceof I32x4TruncSatF64x2UZero) {
			out.write(0x0FD);
			writeUnsignedLEB128(253, out);
		}
		else if(datum instanceof F64x2ConvertLowI32x4S) {
			out.write(0x0FD);
			writeUnsignedLEB128(254, out);
		}
		else if(datum instanceof F64x2ConvertLowI32x4U) {
			out.write(0x0FD);
			writeUnsignedLEB128(255, out);
		}
		else if(datum instanceof F32x4DemoteF64x2Zero) {
			out.write(0x0FD);
			writeUnsignedLEB128(94, out);
		}
		else if(datum instanceof F64x2PromoteLowF32x4) {
			out.write(0x0FD);
			writeUnsignedLEB128(95, out);
		}
		else {
			throw new RuntimeException("Unrecognized instruction type: "+datum.getClass().getCanonicalName());
		}
	}
	
	private static void writeMemArgInstruction(MemArgInstruction datum, OutputStream out) throws IOException{
		writeUnsignedLEB128(datum.getAlign(), out);
		writeUnsignedLEB128(datum.getOffset(), out);
	}
	
	private static void writeBlockType(BlockType datum, OutputStream out) throws IOException{
		if(datum instanceof BlockTypeEmpty) {
			out.write(0x040);
		}
		else if(datum instanceof BlockTypeValueType) {
			writeValueType(((BlockTypeValueType)datum).getValueType(), out);
		}
		else if(datum instanceof BlockTypeIndex) {
			writeSignedLEB128(((BlockTypeIndex)datum).getTypeIndex(),out);
		}
		else {
			throw new RuntimeException("Unrecognized BlockType subclass found: "+datum.getClass().getCanonicalName());
		}
	}
	
	/***********************************************
	 * 
	 * Routines for writing generic data structures
	 * 
	 ***********************************************/
	
	private static <T> void writeVector(Vector<T> v, ElementWriter<T> elementWriter, OutputStream out) throws IOException{
		writeUnsignedLEB128(v.size(), out);
		for(T datum:v)
			elementWriter.write(datum, out);
	}
	
	private static void writeVector(byte[] v, OutputStream out) throws IOException{
		writeUnsignedLEB128(v.length, out);
		out.write(v);
	}

	/**
	 * TODO Double-check whether java floating point encoding using IEEE 754 has any significant differences
	 * with IEEE 754 2019 (used for WASM)
	 * 
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	private static void writeDouble(double value, OutputStream out) throws IOException {
		long bits = Double.doubleToRawLongBits(value);
		for(int i=0;i<4;i++) {
			out.write((int)(bits & 0x0FFL));
			bits>>>=8;
		}
	}

	/**
	 * TODO Double-check whether java floating point encoding using IEEE 754 has any significant differences
	 * with IEEE 754 2019 (used for WASM)
	 * 
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	private static void writeFloat(float value, OutputStream out) throws IOException {
		int bits = Float.floatToRawIntBits(value);
		for(int i=0;i<4;i++) {
			out.write(bits & 0x0FF);
			bits>>>=8;
		}
	}
	
	/**
	 * See: https://en.wikipedia.org/wiki/LEB128
	 * 
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	private static void writeUnsignedLEB128(long value, OutputStream out) throws IOException {
		int b_out;
		do {
			b_out = (int)(value&0x07FL);
			value >>>= 7;
			if(value==0)
				out.write(b_out);
			else
				out.write(b_out|0x080);
		} while(value != 0);
	}
	
	private static int getUnsignedLEB128Size(long value) {
		int byteCount = 0;
		do {
			value >>>= 7;
			byteCount++;
		} while(value != 0);
		return byteCount;
	}

	/**
	 * See: https://en.wikipedia.org/wiki/LEB128
	 * See: https://dwarfstd.org/doc/Dwarf3.pdf (pages 141, 185)
	 * 
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	private static void writeSignedLEB128(long value, OutputStream out) throws IOException {
		boolean more = true;
		int b_out;
		while(more) {
			b_out = (int)(value&0x07FL);
			value >>= 7;
			if(
				((value==0)&&(0==(b_out&0x040)))
				||
				((value==-1)&&(0!=(b_out&0x040)))
			) {
				out.write(b_out);
				more=false;
			}
			else {
				out.write(b_out|0x080);
			}
		}
	}
	
	interface ElementWriter<EW>{
		void write(EW element, OutputStream out) throws IOException;
	};
}
