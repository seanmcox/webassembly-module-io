/**
 * 
 */
package com.shtick.utils.wasm.module.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import com.shtick.utils.wasm.module.Code;
import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.Element;
import com.shtick.utils.wasm.module.ElementSection;
import com.shtick.utils.wasm.module.Export;
import com.shtick.utils.wasm.module.ExportSection;
import com.shtick.utils.wasm.module.Expression;
import com.shtick.utils.wasm.module.FunctionDefinition;
import com.shtick.utils.wasm.module.FunctionType;
import com.shtick.utils.wasm.module.GlobalType;
import com.shtick.utils.wasm.module.Import;
import com.shtick.utils.wasm.module.ImportDescriptor;
import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.Limits;
import com.shtick.utils.wasm.module.Memory;
import com.shtick.utils.wasm.module.MemoryType;
import com.shtick.utils.wasm.module.Module;
import com.shtick.utils.wasm.module.ModuleSerializer;
import com.shtick.utils.wasm.module.Mutability;
import com.shtick.utils.wasm.module.ReferenceType;
import com.shtick.utils.wasm.module.ResultType;
import com.shtick.utils.wasm.module.StartSection;
import com.shtick.utils.wasm.module.Table;
import com.shtick.utils.wasm.module.TableType;
import com.shtick.utils.wasm.module.ValueType;
import com.shtick.utils.wasm.module.ValueTypeInterface;
import com.shtick.utils.wasm.module.Element.ElementMode;
import com.shtick.utils.wasm.module.instructions.BlockTypeIndex;
import com.shtick.utils.wasm.module.instructions.CallByFunctionDefinition;
import com.shtick.utils.wasm.module.instructions.CallImportFunction;
import com.shtick.utils.wasm.module.instructions.GlobalGet;
import com.shtick.utils.wasm.module.instructions.I32Add;
import com.shtick.utils.wasm.module.instructions.I32Const;
import com.shtick.utils.wasm.module.instructions.I32Load;
import com.shtick.utils.wasm.module.instructions.I32Store;
import com.shtick.utils.wasm.module.instructions.I32Sub;
import com.shtick.utils.wasm.module.instructions.If;
import com.shtick.utils.wasm.module.instructions.LocalGet;
import com.shtick.utils.wasm.module.instructions.ReferenceFunction;

/**
 * @author seanmcox
 *
 */
class TestCompileImport {

	@Test
	void testFunctionImport() {
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);
		FunctionType biType = new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes));
		FunctionType triType = new FunctionType(new ResultType(triParamTypes), new ResultType(resultTypes));

		Context context = new Context();
		Import imp = context.addFunctionImport("module", "name", biType);

		Code sumCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Add());
			sumCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition sum = new FunctionDefinition(biType, sumCode);
		context.addFunctionDefinition(sum);
		Code diffCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Sub());
			diffCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition diff = new FunctionDefinition(biType, diffCode);
		context.addFunctionDefinition(diff);
		Code exportCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new LocalGet(new Index(2)));
			instructions.add(new If(new BlockTypeIndex(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)),context),
				new Instruction[] {new CallByFunctionDefinition(sum,context)},
				new Instruction[] {new CallImportFunction(imp,context)}));
			exportCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition export = new FunctionDefinition(triType, exportCode);
		context.addFunctionDefinition(export);
		
		// TODO Consider creating a function abstraction that can encapsulate the type, locals, and body of a function, which would then be translated to
		//      a FunctionSection and a CodeSection
		// See: https://webassembly.github.io/spec/core/binary/modules.html#binary-funcsec
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("binop",context.getFunctionIndex(export)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/function/test.wasm");
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
	void testGlobalImport() {
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);
		FunctionType getIntType = new FunctionType(new ResultType(new Vector<>()), new ResultType(resultTypes));

		Context context = new Context();
		Import dexterImport = new Import("mod", "dexter", new GlobalType(ValueType.NUMTYPE_I32 ,Mutability.CONSTANT));
		Import sinisterImport = new Import("mod", "sinister", new GlobalType(ValueType.NUMTYPE_I32 ,Mutability.VARIABLE));
		context.addNonFunctionImport(dexterImport);
		context.addNonFunctionImport(sinisterImport);

		FunctionDefinition getDexter;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new GlobalGet(context.getGlobalIndex(dexterImport)));
			Code code = new Code(new Vector<>(), new Expression(instructions));
			getDexter = new FunctionDefinition(getIntType, code);
		}
		context.addFunctionDefinition(getDexter);
		FunctionDefinition getSinister;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new GlobalGet(context.getGlobalIndex(sinisterImport)));
			Code code = new Code(new Vector<>(), new Expression(instructions));
			getSinister = new FunctionDefinition(getIntType, code);
		}
		context.addFunctionDefinition(getSinister);
		
		// TODO Add an internal global to the test to verify that the index is being calculated correctly.
		
		// See: https://webassembly.github.io/spec/core/binary/modules.html#binary-funcsec
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("getDexter",context.getFunctionIndex(getDexter)));
			exports.add(new Export("getSinister",context.getFunctionIndex(getSinister)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/global/test.wasm");
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
	void testMemoryImport() {
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> monoType = new Vector<>();
		monoType.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> nullType = new Vector<>();

		Context context = new Context();
		Code setCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Store(0,0));
			setCode = new Code(new Vector<>(), new Expression(instructions));
		}
		Code getCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new I32Load(0, 0));
			getCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition functionSetExport = new FunctionDefinition(new FunctionType(new ResultType(biParamTypes), new ResultType(nullType)), setCode);
		FunctionDefinition functionGetExport = new FunctionDefinition(new FunctionType(new ResultType(monoType), new ResultType(monoType)), getCode);
		context.addFunctionDefinition(functionSetExport);
		context.addFunctionDefinition(functionGetExport);
		context.addNonFunctionImport(new Import("module", "memory", new MemoryType(new Limits(1, 1))));
		
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("set",context.getFunctionIndex(functionSetExport)));
			exports.add(new Export("get",context.getFunctionIndex(functionGetExport)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/memory/test.wasm");
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
	void testTableImport() {
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> monoType = new Vector<>();
		monoType.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> nullType = new Vector<>();

		Context context = new Context();
		Code aCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new I32Const(256));
			aCode = new Code(new Vector<>(), new Expression(instructions));
		}
		Code bCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new I32Const(512));
			bCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition functionAExport = new FunctionDefinition(new FunctionType(new ResultType(nullType), new ResultType(monoType)), aCode);
		FunctionDefinition functionBExport = new FunctionDefinition(new FunctionType(new ResultType(nullType), new ResultType(monoType)), bCode);
		context.addFunctionDefinition(functionAExport);
		context.addFunctionDefinition(functionBExport);
		Import tableImport = new Import("module", "table", new TableType(ReferenceType.REFTYPE_FUNCREF, new Limits(2, -1)));
		context.addNonFunctionImport(tableImport);
		
		ExportSection exportSection = new ExportSection(new Vector<>());
		StartSection startSection = null;
		ElementSection elementSection;
		{
			Vector<Element> elements = new Vector<>();
			{
				Vector<Expression> expressions = new Vector<>();
				LinkedList<Instruction> zero = new LinkedList<>();
				zero.add(new I32Const(0));
				{
					LinkedList<Instruction> instructions = new LinkedList<>();
					instructions.add(new ReferenceFunction(context.getFunctionIndex(functionBExport)));
					expressions.add(new Expression(instructions));
				}
				{
					LinkedList<Instruction> instructions = new LinkedList<>();
					instructions.add(new ReferenceFunction(context.getFunctionIndex(functionAExport)));
					expressions.add(new Expression(instructions));
				}
				elements.add(new Element(ReferenceType.REFTYPE_FUNCREF, expressions, ElementMode.ACTIVE, context.getTableIndex(tableImport), new Expression(zero)));
			}
			elementSection = new ElementSection(elements);
		}
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/table/test.wasm");
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
}
