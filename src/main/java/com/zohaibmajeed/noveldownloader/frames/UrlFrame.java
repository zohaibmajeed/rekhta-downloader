package com.zohaibmajeed.noveldownloader.frames;

import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class UrlFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private Consumer<String> onDownload;
	private Runnable onCancel;

	public UrlFrame() {
		setSize(400, 240);
		setTitle("Enter Url - Rekhta Downloader");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new MigLayout("fill, inset 20", "[grow]", "[][][grow]"));
		WindowUtils.setIcon(this);

		JLabel lblEnterUrl = new JLabel("Enter URL");
		getContentPane().add(lblEnterUrl, "growx, wrap");

		textField = new JTextField();
		textField.setColumns(10);
		getContentPane().add(textField, "growx, wrap");
		TextPopup.attach(textField);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, "flowx,cell 0 2,alignx right,aligny bottom");

		JButton btnDownload = new JButton("Download");
		panel.add(btnDownload);
		btnDownload.addActionListener(_ -> {
			if (onDownload != null)
				onDownload.accept(textField.getText());
		});
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel);
		btnCancel.addActionListener(_ -> {
			if (onCancel != null)
				onCancel.run();
		});
	}

	public void addDownloadListener(Consumer<String> c) {
		onDownload = c;
	}
	
	public void addCancelListener(Runnable c) {
		onCancel = c;
	}
}
