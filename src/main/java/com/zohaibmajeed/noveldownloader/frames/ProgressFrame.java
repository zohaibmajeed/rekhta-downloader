package com.zohaibmajeed.noveldownloader.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.Taskbar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

public class ProgressFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel lblPages;
	private JLabel lblDownloaded;
	private JLabel lblPercentage;
	private JProgressBar barProgress;
	private Runnable onCancel;

	public ProgressFrame() {
		setSize(400, 240);
		setTitle("Downloading - Rekhta Downloader");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel.run();
			}
		});
		WindowUtils.setIcon(this);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0, 2, 5, 15));
		mainPanel.add(infoPanel);

		JLabel lblTotalPages = new JLabel("Total Pages");
		infoPanel.add(lblTotalPages);

		lblPages = new JLabel("Checking...");
		infoPanel.add(lblPages);

		JLabel lblDownloadedPages = new JLabel("Downloaded Pages");
		infoPanel.add(lblDownloadedPages);

		lblDownloaded = new JLabel("0");
		infoPanel.add(lblDownloaded);

		JLabel lblProgress = new JLabel("Progress");
		infoPanel.add(lblProgress);

		lblPercentage = new JLabel("0%");
		infoPanel.add(lblPercentage);

		JPanel barPanel = new JPanel();
		barPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
		mainPanel.add(barPanel);
		barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));

		barProgress = new JProgressBar();
		barProgress.setValue(0);
		barPanel.add(barProgress);

		JPanel buttonsPanel = new JPanel();
		mainPanel.add(buttonsPanel);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(cancelButton);
		cancelButton.addActionListener(_ -> {
			if (onCancel != null)
				onCancel.run();
		});
	}

	public void setProgress(int total, int completed) {
		int percentage = completed * 100 / total;

		lblPages.setText(total + "");
		lblDownloaded.setText(completed + "");
		lblPercentage.setText(percentage + "%");
		barProgress.setValue((int) percentage);

		if (Taskbar.isTaskbarSupported()) {
			Taskbar taskbar = Taskbar.getTaskbar();
			if (taskbar.isSupported(Taskbar.Feature.PROGRESS_VALUE))
				Taskbar.getTaskbar().setProgressValue(percentage);
		}
	}

	public void addCancelListener(Runnable c) {
		onCancel = c;
	}
}
