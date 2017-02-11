/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.directory;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.omg.CORBA.ORB;
import files.directory;
import files.directoryHelper;
import files.ui.directory.controller.FilesListController;
import files.ui.directory.controller.MenuController;

/**
 * The GUI client of {@link files.Serveur}
 *
 * @author Jimmy Tournemaine
 */
public class ClientUI extends JFrame {

    private static final long serialVersionUID = 4368635630731592525L;
    private JPanel contentPane;

    public static final String NEW_DIRECTORY = "new_directory";
    public static final String NEW_FILE = "new_file";
    public static final String OPEN_DIRECTORY = "open_directory";
    public static final String OPEN_FILE = "open_file";
    public static final String DELETE = "delete";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
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
        setBounds(100, 100, 593, 300);
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
        btnNewDirectory.setName(NEW_DIRECTORY);
        ctrlPanel.add(btnNewDirectory);
        btnNewDirectory.addActionListener(controller);

        JButton btnNewFile = new JButton("New File");
        btnNewFile.setName(NEW_FILE);
        ctrlPanel.add(btnNewFile);
        btnNewFile.addActionListener(controller);

        JButton btnOpenDirectory = new JButton("Open Directory");
        btnOpenDirectory.setName(OPEN_DIRECTORY);
        ctrlPanel.add(btnOpenDirectory);
        btnOpenDirectory.addActionListener(controller);

        JButton btnOpenFile = new JButton("Open File");
        btnOpenFile.setName(OPEN_FILE);
        ctrlPanel.add(btnOpenFile);
        btnOpenFile.addActionListener(controller);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setName(DELETE);
        ctrlPanel.add(btnDelete);
        btnDelete.addActionListener(controller);
    }

    /**
     * Initialize the client and get the root representation of the remote file system.
     */
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
