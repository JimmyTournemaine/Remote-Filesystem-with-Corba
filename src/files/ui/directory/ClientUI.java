package files.ui.directory;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.ORB;

import files.directory;
import files.directoryHelper;
import files.ui.directory.controller.FilesListController;
import files.ui.directory.controller.MenuController;

import javax.swing.JButton;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 4368635630731592525L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI frame = new ClientUI();
					frame.setTitle("Remote Filesystem");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		directory root = init();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		FilesListView list = new FilesListView();
		FilesListModel model = new FilesListModel(root);
		FilesListController controller = new FilesListController(model, list);
		new MenuController(model, list);
		
		JPanel ctrlPanel = new JPanel();
		
		contentPane.add(list, BorderLayout.CENTER);
		contentPane.add(ctrlPanel, BorderLayout.NORTH);
		
		JButton btnNewDirectory = new JButton("New Directory");
		btnNewDirectory.setName("new_directory");
		ctrlPanel.add(btnNewDirectory);
		btnNewDirectory.addActionListener(controller);
		
		JButton btnNewFile = new JButton("New File");
		btnNewFile.setName("new_file");
		ctrlPanel.add(btnNewFile);
		btnNewFile.addActionListener(controller);
		
		JButton btnOpenDirectory = new JButton("Open Directory");
		btnOpenDirectory.setName("open_directory");
		ctrlPanel.add(btnOpenDirectory);
		btnOpenDirectory.addActionListener(controller);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.setName("open_file");
		ctrlPanel.add(btnOpenFile);
		btnOpenFile.addActionListener(controller);
	}
	
	private directory init() {
		ORB orb = ORB.init(new String[0], null);
		String ior = null;

		try {
			String ref = "files.ref";
			FileInputStream file = new FileInputStream(ref);
			BufferedReader in = new BufferedReader(new InputStreamReader(file));
			ior = in.readLine();
			file.close();
		} catch (IOException ex) {
			System.err.println("Cannot read file : `" + ex.getMessage() + "'");
			System.exit(1);
		}


		org.omg.CORBA.Object obj = orb.string_to_object(ior);

		if (obj == null) {
			System.err.println("Erreur when doing string_to_object() ");
			throw new RuntimeException();
		}

		directory root = directoryHelper.narrow(obj);

		if (root == null) {
			System.err.println("Erreur when doing narrow() ");
			throw new RuntimeException();
		}
		
		return root;
	}

}
