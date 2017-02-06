package files.ui.directory;

import javax.swing.DefaultListModel;

import files.directory;
import files.directory_entry;
import files.directory_entryHolder;
import files.file_listHolder;
import files.file_type;

public class FilesListModel extends DefaultListModel {

	private static final long serialVersionUID = 5037457163325723718L;
	private directory current;
	
	public FilesListModel(directory current) {
		addParent();
		setCurrent(current);
	}
	
	public void setCurrent(directory current) {
		this.current = current;
		refresh();
	}
	
	public directory getCurrent() {
		return this.current;
	}
	
	public void refresh() {
		directory_entryHolder eh = new directory_entryHolder(new directory_entry("", file_type.regular_file_type));
		file_listHolder lh = new file_listHolder();
		current.list_files(lh);
		this.clear();
		this.addParent();
		while (lh.value.next_one(eh)) {
			this.addElement(eh.value);
		}
	}
	
	public void addParent() {
		super.addElement(new directory_entry("..", file_type.directory_type));
	}
	
	public void add(int index, directory_entry element) {
		super.add(index, element);
	}
	
	public void addElement(directory_entry element) {
		super.addElement(element);
	}

}
