package com.zohaibmajeed.noveldownloader.frames;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DownloadCompletedFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtFilePath;
	private Runnable onCancel;

	public DownloadCompletedFrame() {
		setTitle("Download Completed - Rekhta Downloader");
		setSize(400, 240);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WindowUtils.setIcon(this);

		getContentPane().setLayout(new MigLayout("fill, inset 20", "[grow]", "[][][grow]"));

		JLabel lblFilePath = new JLabel("File Path");
		getContentPane().add(lblFilePath, "cell 0 0");

		txtFilePath = new JTextField();
		txtFilePath.setText("File Path");
		txtFilePath.setEditable(false);
		getContentPane().add(txtFilePath, "cell 0 1,growx");
		txtFilePath.setColumns(10);
		TextPopup.attach(txtFilePath, false);

		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 2,alignx right,aligny bottom");

		JButton btnOpen = new JButton("Open");
		panel.add(btnOpen);
		btnOpen.addActionListener(_ -> {
			try {
				Path file = Path.of(txtFilePath.getText());
				Desktop.getDesktop().open(file.toFile());
			} catch (IOException ev) {
			}
		});

		JButton btnOpenFolder = new JButton("Open Folder");
		panel.add(btnOpenFolder);
		btnOpenFolder.addActionListener(_ -> {
			try {
				File file = Path.of(txtFilePath.getText()).getParent().toFile();
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
			}
		});

		JButton btnClose = new JButton("Close");
		panel.add(btnClose);
		btnClose.addActionListener(_ -> {
			if (onCancel != null)
				onCancel.run();
		});
	}

	public void setFilePath(String path) {
		txtFilePath.setText(path);
	}

	public void addCancelListener(Runnable c) {
		onCancel = c;
	}
}
