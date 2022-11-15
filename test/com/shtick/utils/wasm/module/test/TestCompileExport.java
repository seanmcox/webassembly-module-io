/**
 * 
 */
package com.shtick.utils.wasm.module.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import com.shtick.utils.wasm.module.Code;
import com.shtick.utils.wasm.module.CodeSection;
import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.DataSection;
import com.shtick.utils.wasm.module.ElementSection;
import com.shtick.utils.wasm.module.Export;
import com.shtick.utils.wasm.module.ExportDescriptor;
import com.shtick.utils.wasm.module.ExportSection;
import com.shtick.utils.wasm.module.Expression;
import com.shtick.utils.wasm.module.FunctionDefinition;
import com.shtick.utils.wasm.module.FunctionIndex;
import com.shtick.utils.wasm.module.FunctionSection;
import com.shtick.utils.wasm.module.FunctionType;
import com.shtick.utils.wasm.module.GlobalSection;
import com.shtick.utils.wasm.module.ImportSection;
import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.MemorySection;
import com.shtick.utils.wasm.module.Module;
import com.shtick.utils.wasm.module.ModuleSerializer;
import com.shtick.utils.wasm.module.ResultType;
import com.shtick.utils.wasm.module.StartSection;
import com.shtick.utils.wasm.module.TableSection;
import com.shtick.utils.wasm.module.TypeIndex;
import com.shtick.utils.wasm.module.TypeSection;
import com.shtick.utils.wasm.module.ValueType;
import com.shtick.utils.wasm.module.instructions.BlockTypeIndex;
import com.shtick.utils.wasm.module.instructions.CallByFunctionDefinition;
import com.shtick.utils.wasm.module.instructions.I32Add;
import com.shtick.utils.wasm.module.instructions.I32Sub;
import com.shtick.utils.wasm.module.instructions.If;
import com.shtick.utils.wasm.module.instructions.LocalGet;

/**
 * @author seanmcox
 *
 */
class TestCompileExport {

	@Test
	void testFunctionExport() {
		TypeSection typeSection;
		{
			Vector<FunctionType> functypes = new Vector<>();
			Vector<ValueType> paramTypes = new Vector<>();
			paramTypes.add(ValueType.NUMTYPE_I32);
			paramTypes.add(ValueType.NUMTYPE_I32);
			Vector<ValueType> resultTypes = new Vector<>();
			resultTypes.add(ValueType.NUMTYPE_I32);
			functypes.add(new FunctionType(new ResultType(paramTypes), new ResultType(resultTypes)));
			typeSection = new TypeSection(functypes);
		}
		ImportSection importSection = new ImportSection(new Vector<>());
		FunctionSection functionSection;
		{
			Vector<TypeIndex> typeIndexes = new Vector<>();
			typeIndexes.add(new TypeIndex(0));
			functionSection = new FunctionSection(typeIndexes);
		}
		TableSection tableSection = new TableSection(new Vector<>());
		MemorySection memorySection = new MemorySection(new Vector<>());
		GlobalSection globalSection = new GlobalSection(new Vector<>());
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("add",new FunctionIndex(0)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		CodeSection codeSection;
		{
			Vector<Code> codes = new Vector<>();
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Add());
			codes.add(new Code(new Vector<>(), new Expression(instructions)));
			codeSection = new CodeSection(codes);
		}
		DataSection dataSection = new DataSection(new Vector<>());
		Module module = new Module(
			typeSection,importSection,functionSection,tableSection,memorySection,globalSection,exportSection,startSection,elementSection,
			codeSection,dataSection
		);
		
//		try(ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
		File outfile = new File("test_dist/export/function/test.wasm");
		try(FileOutputStream out = new FileOutputStream(outfile)) {
			ModuleSerializer.writeModule(module, out);
		}
		catch(IOException t) {
			t.printStackTrace();
			fail("IOException thrown");
		}
		catch(Throwable t){
			t.printStackTrace();
			fail("Throwable thrown");
		}
	}

	@Test
	void testSubroutineCall() {
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);

		Context context = new Context();
		Code sumCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Add());
			sumCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition sum = new FunctionDefinition(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)), sumCode);
		context.addFunctionDefinition(sum);
		Code diffCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Sub());
			diffCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition diff = new FunctionDefinition(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)), diffCode);
		context.addFunctionDefinition(diff);
		Code exportCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new LocalGet(new Index(2)));
			instructions.add(new If(new BlockTypeIndex(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)),context),
				new Instruction[] {new CallByFunctionDefinition(sum,context)},
				new Instruction[] {new CallByFunctionDefinition(diff,context)}));
			exportCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition export = new FunctionDefinition(new FunctionType(new ResultType(triParamTypes), new ResultType(resultTypes)), exportCode);
		context.addFunctionDefinition(export);
		
		TableSection tableSection = new TableSection(new Vector<>());
		MemorySection memorySection = new MemorySection(new Vector<>());
		GlobalSection globalSection = new GlobalSection(new Vector<>());
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("binop",context.getFunctionIndex(export)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		DataSection dataSection = new DataSection(new Vector<>());
		Module module = new Module(
			context,tableSection,memorySection,globalSection,exportSection,startSection,elementSection,
			dataSection
		);
		
//		try(ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
		File outfile = new File("test_dist/export/subroutine/test.wasm");
		try(FileOutputStream out = new FileOutputStream(outfile)) {
			ModuleSerializer.writeModule(module, out);
		}
		catch(IOException t) {
			t.printStackTrace();
			fail("IOException thrown");
		}
		catch(Throwable t){
			t.printStackTrace();
			fail("Throwable thrown");
		}
	}

	@Test
	void testGlobalExport() {
		// TODO
	}

	@Test
	void testMemoryExport() {
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);

		Context context = new Context();
		Code sumCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Add());
			sumCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition sum = new FunctionDefinition(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)), sumCode);
		context.addFunctionDefinition(sum);
		Code diffCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Sub());
			diffCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition diff = new FunctionDefinition(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)), diffCode);
		context.addFunctionDefinition(diff);
		Code exportCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new LocalGet(new Index(2)));
			instructions.add(new If(new BlockTypeIndex(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)),context),
				new Instruction[] {new CallByFunctionDefinition(sum,context)},
				new Instruction[] {new CallByFunctionDefinition(diff,context)}));
			exportCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition export = new FunctionDefinition(new FunctionType(new ResultType(triParamTypes), new ResultType(resultTypes)), exportCode);
		context.addFunctionDefinition(export);
		
		TableSection tableSection = new TableSection(new Vector<>());
		MemorySection memorySection = new MemorySection(new Vector<>());
		GlobalSection globalSection = new GlobalSection(new Vector<>());
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("binop",context.getFunctionIndex(export)));
			exports.add(new Export("exmem",MemoryIndex));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		DataSection dataSection = new DataSection(new Vector<>());
		Module module = new Module(
			context,tableSection,memorySection,globalSection,exportSection,startSection,elementSection,
			dataSection
		);
		
//		try(ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
		File outfile = new File("test_dist/export/memory/test.wasm");
		try(FileOutputStream out = new FileOutputStream(outfile)) {
			ModuleSerializer.writeModule(module, out);
		}
		catch(IOException t) {
			t.printStackTrace();
			fail("IOException thrown");
		}
		catch(Throwable t){
			t.printStackTrace();
			fail("Throwable thrown");
		}
	}

	@Test
	void testTableExport() {
		// TODO
	}
}
