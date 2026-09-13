package com.zohaibmajeed.noveldownloader.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class BookProcessor {
	private BookInfo bookInfo;
	private ArrayList<Path> pagePaths;
	private Path outputPath;
	private Consumer<BookProgress> onProgress;
	private boolean isCanceled;

	public BookProcessor(String providedUrl) throws IOException, InterruptedException {
		bookInfo = new BookInfo(providedUrl);
		pagePaths = new ArrayList<Path>();
		outputPath = Paths.get(System.getProperty("user.home"), "Downloads", bookInfo.getSlug() + ".pdf");
		isCanceled = false;
	}

	public static class BookProgress {
		public int total;
		public int downloaded;

		public BookProgress(int total, int downloaded) {
			this.total = total;
			this.downloaded = downloaded;
		}
	}

	private void notifyProgress() {
		BookProgress progress = new BookProgress(bookInfo.getPagesCount(), pagePaths.size());
		onProgress.accept(progress);
	}

	public Path process() throws IOException, InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(5);

		while (!isCanceled && pagePaths.size() < bookInfo.getPagesCount()) {
			ArrayList<String> ids = bookInfo.findIds(pagePaths.size());
			ArrayList<Future<Path>> paths = new ArrayList<Future<Path>>();

			for (int i = 0; i < ids.size(); i++) {
				String bookId = bookInfo.getBookId();
				String pageId = ids.get(i);
				int pageNumber = pagePaths.size() + i + 1;
				Callable<Path> task = () -> PageProcessor.process(bookId, pageId, pageNumber);
				paths.add(pool.submit(task));
			}

			for (Future<Path> path : paths) {
				pagePaths.add(path.get());
				notifyProgress();
			}
		}

		if (!isCanceled)
			PDFMaker.make(pagePaths, outputPath);

		for (Path path : pagePaths)
			Files.deleteIfExists(path);

		return outputPath;
	}

	public void addProgressListner(Consumer<BookProgress> c) {
		onProgress = c;
		notifyProgress();
	}

	public void cancel() {
		isCanceled = false;
	}

	public boolean isCanceledOut() {
		return isCanceled;
	}

}
