package com.zohaibmajeed.noveldownloader.frames;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.awt.Taskbar;

public class WindowUtils {
	public static void setIcon(JFrame frame) {
		URL iconUrl = WindowUtils.class.getResource("/favicon.png");
		if (iconUrl == null) {
			iconUrl = WindowUtils.class.getResource("/resources/favicon.png");
		}
		ImageIcon icon = new ImageIcon(iconUrl);
		frame.setIconImage(icon.getImage());

		if (Taskbar.isTaskbarSupported()) {
			Taskbar taskbar = Taskbar.getTaskbar();
			if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE))
				taskbar.setIconImage(icon.getImage());
		}
	}
}
