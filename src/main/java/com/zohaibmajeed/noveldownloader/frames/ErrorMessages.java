package com.zohaibmajeed.noveldownloader.frames;

import javax.swing.JOptionPane;

public class ErrorMessages {
	public static int RETRY = 0;

	public static int infoWithRetry(String title, String message) {
		int result = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE, null, new Object[] { "Retry", "Cancel" }, "Retry");

		return result;
	}

	public static void info(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
