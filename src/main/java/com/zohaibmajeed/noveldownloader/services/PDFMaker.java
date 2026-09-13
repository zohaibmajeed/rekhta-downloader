package com.zohaibmajeed.noveldownloader.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFMaker {
	private PDDocument book;

	public PDFMaker() {
		book = new PDDocument();
	}

	public void addPage(Path imagePath) throws IOException {
		PDImageXObject image = PDImageXObject.createFromFile(imagePath.toString(), book);
		
		PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
		book.addPage(page);
		
		PDPageContentStream content = new PDPageContentStream(book, page);
		content.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
		content.close();
	}

	public void saveTo(Path filePath) throws IOException {
		book.save(filePath.toFile());
	}
	
	public static void make(ArrayList<Path> imagePaths, Path saveTo) throws IOException {
		PDFMaker maker = new PDFMaker();
		for (Path imagePath : imagePaths) 
			maker.addPage(imagePath);
		maker.saveTo(saveTo);
	}
}
