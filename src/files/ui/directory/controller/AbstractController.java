package files.ui.directory.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import files.access_denied;
import files.already_exist;
import files.directoryHolder;
import files.directory_entry;
import files.invalid_type_file;
import files.io;
import files.mode;
import files.no_such_file;
import files.regular_fileHolder;
import files.ui.directory.FilesListModel;
import files.ui.directory.FilesListView;
import files.ui.regular.RegularFileDialog;

class AbstractController implements ActionListener {

	protected FilesListModel model;
	protected FilesListView view;

	protected static final String NEW_DIRECTORY = "new_directory";
	protected static final String NEW_FILE = "new_file";
	protected static final String OPEN_DIRECTORY = "open_directory";
	protected static final String OPEN_FILE = "open_file";

	AbstractController(FilesListModel model, FilesListView view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * Open the selected directory
	 */
	protected void openDirectory() {
		directoryHolder dirHolder = new directoryHolder();
		directory_entry entry = (directory_entry) this.view.getSelectedValue();
		if (entry == null)
			return;
		try {
			this.model.getCurrent().open_directory(dirHolder, entry.name);
			this.model.setCurrent(dirHolder.value);
		} catch (no_such_file e1) {
			showErrorMessage(e1, entry.name + " does not exist.");
		} catch (invalid_type_file e1) {
			showErrorMessage(e1, entry.name + " is not a regular file.");
		} catch (access_denied e1) {
			showErrorMessage(e1, "Cannot open directory : access denied.");
		}
	}

	/**
	 * Open the selected file
	 */
	protected void openFile() {
		regular_fileHolder fileHolder = new regular_fileHolder();
		directory_entry entry = (directory_entry) this.view.getSelectedValue();
		if (entry == null)
			return;
		try {
			this.model.getCurrent().open_regular_file(fileHolder, entry.name, mode.read_write);
			new RegularFileDialog(fileHolder.value);
		} catch (no_such_file e1) {
			showErrorMessage(e1, entry.name + " does not exist.");
		} catch (invalid_type_file e1) {
			showErrorMessage(e1, entry.name + " is not a regular file.");
		} catch (io e1) {
			e1.printStackTrace();
		}
	}

	protected void newDirectory() {
		String name = JOptionPane.showInputDialog("Enter the name of the new directory :");
		if (name == null || name.equals(""))
			return;
		try {
			this.model.getCurrent().create_directory(new directoryHolder(), name);
			this.model.refresh();
		} catch (already_exist e1) {
			JOptionPane.showMessageDialog(view, "The name " + name + " is already used.");
		} catch (io e1) {
			e1.printStackTrace();
		}
	}

	protected void newFile() {
		String name = JOptionPane.showInputDialog("Enter the name of the new directory :");
		if (name == null)
			return;
		try {
			this.model.getCurrent().create_regular_file(new regular_fileHolder(), name);
			this.model.refresh();
		} catch (already_exist e1) {
			JOptionPane.showMessageDialog(view, "The name " + name + " is already used.");
		} catch (io e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Display an error message
	 * 
	 * @param exception
	 * @param message
	 */
	private void showErrorMessage(Exception exception, String message) {
		String title = null;
		if (exception instanceof no_such_file) {
			title = "No such file or directory";
		} else if (exception instanceof invalid_type_file) {
			title = "Invalid type";
		} else if (exception instanceof access_denied) {
			title = "Access denied";
		} else {
			title = "Error";
		}
		JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent) e.getSource();

		if (source.getName() == null) {
			System.err.println("The source " + source + " must have a handled name.");
			return;
		}

		if (source.getName().equals(NEW_DIRECTORY)) {
			newDirectory();
		} else if (source.getName().equals(NEW_FILE)) {
			newFile();
		} else if (source.getName().equals(OPEN_DIRECTORY)) {
			openDirectory();
		} else if (source.getName().equals(OPEN_FILE)) {
			openFile();
		}
	}
}
