/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
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
import files.no_such_file;
import files.regular_fileHolder;
import files.ui.directory.ClientUI;
import files.ui.directory.FilesListModel;
import files.ui.directory.FilesListView;
import files.ui.regular.RegularFileDialog;

/**
 * A controller of the file list.
 */
class AbstractController implements ActionListener {

	protected FilesListModel model;
	protected FilesListView view;

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
		} catch (io e) {
			showErrorMessage(e, "An internal server error occurred.");
		}
	}

	/**
	 * Open the selected file
	 */
	protected void openFile() {
		directory_entry entry = (directory_entry) this.view.getSelectedValue();
		if (entry == null)
			return;
		try {
			new RegularFileDialog(this.model.getCurrent(), entry.name);
		} catch (no_such_file e1) {
			showErrorMessage(e1, entry.name + " does not exist.");
		} catch (invalid_type_file e1) {
			showErrorMessage(e1, entry.name + " is not a regular file.");
		} catch (access_denied e1) {
			showErrorMessage(e1, entry.name+" : access denied.");
		} catch (io e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Create a new directory
	 */
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
		} catch(access_denied e1) {
			showErrorMessage(e1, "Cannot create "+name+" directory : access denied.");
		}
	}

	/**
	 * Create a new file
	 */
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
		} catch(access_denied e1) {
			showErrorMessage(e1, "Cannot create file "+name+" : access denied.");
		}
	}
	
	/**
	 * Delete the selected file
	 */
	protected void delete() {
		directory_entry entry = (directory_entry) this.view.getSelectedValue();
		if (entry == null || entry == this.model.firstElement()) // first is parent
			return;
		try {
			if(JOptionPane.showConfirmDialog(view, "Are you sure to delete "+entry.name+" ?", null, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
				this.model.getCurrent().delete_file(entry.name);
				this.model.refresh();
			}
		} catch (Exception e1) {
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

		if (source.getName().equals(ClientUI.NEW_DIRECTORY)) {
			newDirectory();
		} else if (source.getName().equals(ClientUI.NEW_FILE)) {
			newFile();
		} else if (source.getName().equals(ClientUI.OPEN_DIRECTORY)) {
			openDirectory();
		} else if (source.getName().equals(ClientUI.OPEN_FILE)) {
			openFile();
		} else if (source.getName().equals(ClientUI.DELETE)) {
			delete();
		}
	}
}
