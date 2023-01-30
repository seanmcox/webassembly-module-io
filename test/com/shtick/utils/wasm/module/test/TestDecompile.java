/**
 * 
 */
package com.shtick.utils.wasm.module.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.shtick.utils.wasm.module.Module;
import com.shtick.utils.wasm.module.ModuleDeserializer;
import com.shtick.utils.wasm.module.ModuleSerializer;

/**
 * This test helped me to identify a few bugs, but it became a boondogle when analysing differences in the Code Section
 * because the Rust compiled code uses weird (inefficient and unpredictably inefficient) number encoding.
 * 
 * eg. 0 was often encoded as 0x80, 0x80, 0x80, 0x80, 0 instead of just a single byte, 0, but with opcode 65 (0x41) where
 * a signed LEB128 accompanies the opcode, I would sometimes find some numbers encoded efficiently/canonically, and others
 * encoded inefficiently. (I gave up when I found cases with the same number encoded two different ways. Some kind of watermark?)
 * 
 * TODO It would be nice to have a good test, of decoding/re-encoding, but I'll probably have to use modules compiles with this library
 * once a I have some good ones.
 * 
 * @author seanmcox
 *
 */
class TestDecompile {

	@Test
	void testSerialization() {
		for(String filename:new String[] {"test_dist/decompile/dnd_wasm.wasm","test_dist/decompile/mandelbrot_wasm.wasm"}) {
			File file = new File(filename);
			try(FileInputStream in = new FileInputStream(file)){
				byte[] inData;
				inData = in.readAllBytes();
				Module module = ModuleDeserializer.readModule(new ByteArrayInputStream(inData));
				ByteArrayOutputStream out = new ByteArrayOutputStream(inData.length);
				ModuleSerializer.writeModule(module, out);
				byte[] outData = out.toByteArray();
				assertTrue(Arrays.equals(inData, outData),"Input data matched output data for "+filename);
			}
			catch(IOException t) {
				t.printStackTrace();
				fail("IOException thrown.");
			}
		}
	}
}
