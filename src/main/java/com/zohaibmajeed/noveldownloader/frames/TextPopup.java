package com.zohaibmajeed.noveldownloader.frames;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;

public class TextPopup {

	public static void attach(JTextField textField, boolean editEnabled) {
		JPopupMenu popup = new JPopupMenu();

		Action cutAction = textField.getActionMap().get(DefaultEditorKit.cutAction);
		JMenuItem cut = new JMenuItem();
		cut.setAction(cutAction);
		cut.setText("Cut");
		cut.setEnabled(editEnabled);

		Action copyAction = textField.getActionMap().get(DefaultEditorKit.copyAction);
		JMenuItem copy = new JMenuItem();
		copy.setAction(copyAction);
		copy.setText("Copy");

		Action pasteAction = textField.getActionMap().get(DefaultEditorKit.pasteAction);
		JMenuItem paste = new JMenuItem();
		paste.setAction(pasteAction);
		paste.setText("Paste");
		paste.setEnabled(editEnabled);

		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(_ -> {
			int start = textField.getSelectionStart();
			int end = textField.getSelectionEnd();
			if (start != end) {
				textField.replaceSelection("");
			}
		});
		delete.setEnabled(editEnabled);

		Action selectAllAction = textField.getActionMap().get(DefaultEditorKit.selectAllAction);
		JMenuItem selectAll = new JMenuItem();
		selectAll.setAction(selectAllAction);
		selectAll.setText("Select All");

		popup.add(cut);
		popup.add(copy);
		popup.add(paste);
		popup.add(delete);
		popup.addSeparator();
		popup.add(selectAll);

		textField.setComponentPopupMenu(popup);
	}
	
	public static void attach(JTextField textField) {
		attach(textField, true);
	}
}