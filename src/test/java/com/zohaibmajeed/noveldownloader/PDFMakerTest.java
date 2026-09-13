package com.zohaibmajeed.noveldownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

import com.zohaibmajeed.noveldownloader.services.PDFMaker;

import junit.framework.TestCase;

public class PDFMakerTest extends TestCase {
	void testMakesPdf() throws IOException {
		PDFMaker maker = new PDFMaker();
		System.out.println("Initialized.");
		
		maker.addPage(Path.of("/tmp/output.png"));
		System.out.println("Page added.");
		
		maker.saveTo(Path.of("/tmp/output.pdf"));
		System.out.println("Output saved.");
		
		boolean outputExists = Files.exists(Path.of("/tmp/output.pdf"));
		assertTrue(outputExists);
	}
}
