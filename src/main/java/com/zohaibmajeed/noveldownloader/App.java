package com.zohaibmajeed.noveldownloader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.zohaibmajeed.noveldownloader.frames.ErrorMessages;
import com.zohaibmajeed.noveldownloader.frames.DownloadCompletedFrame;
import com.zohaibmajeed.noveldownloader.frames.ProgressFrame;
import com.zohaibmajeed.noveldownloader.frames.UrlFrame;
import com.zohaibmajeed.noveldownloader.services.BookProcessor;

public class App {
	private BookProcessor processor;
	private Thread processingThread;

	private UrlFrame urlFrame;
	private ProgressFrame progressFrame;
	private DownloadCompletedFrame downloadCompletedFrame;

	public App() {
		urlFrame = new UrlFrame();
		progressFrame = new ProgressFrame();
		downloadCompletedFrame = new DownloadCompletedFrame();

		urlFrame.addDownloadListener(url -> {
			urlFrame.setVisible(false);
			progressFrame.setVisible(true);

			processingThread = new Thread(() -> {
				try {
					processor = new BookProcessor(url);
					processor.addProgressListner(progress -> {
						SwingUtilities.invokeLater(() -> {
							progressFrame.setProgress(progress.total, progress.downloaded);
						});
					});
				} catch (IOException | InterruptedException e) {
					SwingUtilities.invokeLater(() -> {
						ErrorMessages.info("Error",
								"There was an error while fetching book information. Make sure URL is from rekhta website and internet connection is working.");
						progressFrame.setVisible(false);
						urlFrame.setVisible(true);
					});
					return;
				}

				while (true)
					try {
						Path outputPath = processor.process();

						if (!processor.isCanceledOut())
							SwingUtilities.invokeLater(() -> {
								progressFrame.setVisible(false);
								downloadCompletedFrame.setFilePath(outputPath.toString());
								downloadCompletedFrame.setVisible(true);
							});

						return;
					} catch (ExecutionException | IOException | InterruptedException e) {
						System.out.println("Exception occured.");
						try {
							SwingUtilities.invokeAndWait(() -> {
								if (ErrorMessages.infoWithRetry("Error",
										"There was an error while trying to download book page. Make sure internet connection is working and then click retry.") != ErrorMessages.RETRY)
									processor.cancel();
							});
						} catch (InvocationTargetException | InterruptedException e1) {
							processor.cancel();
							return;
						}
					}
			});

			processingThread.start();
		});

		urlFrame.addCancelListener(() -> {
			System.exit(0);
		});

		progressFrame.addCancelListener(() -> {
			int result = JOptionPane.showConfirmDialog(progressFrame,
					"Process will be stopped. Are you sure you want to exit?", "Confirm Exit",
					JOptionPane.YES_NO_OPTION);

			if (result != JOptionPane.YES_OPTION)
				return;

			progressFrame.setVisible(false);
			processor.cancel();
			try {
				processingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
		});

		downloadCompletedFrame.addCancelListener(() -> {
			System.exit(0);
		});

		urlFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new App();
	}
}
