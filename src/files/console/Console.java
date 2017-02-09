/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.omg.CORBA.ORB;
import files.directory;
import files.directoryHelper;
import files.regular_file;
import files.console.command.Command;
import files.console.command.CreateDirectory;
import files.console.command.CreateFile;
import files.console.command.DeleteFile;
import files.console.command.FolderCommand;
import files.console.command.ListFiles;
import files.console.command.OpenDirectory;
import files.console.command.OpenFile;
import files.console.command.ReadCommand;
import files.console.command.SeekCommand;
import files.console.command.WriteCommand;

/**
 * The console client of {@link files.Serveur}
 */
public class Console {

    public Console() {
        super();
    }

    public static void main(String[] argv) throws IOException {

        ORB orb = ORB.init(argv, null);
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

        folderLoop(root);
    }

    /**
     * The loop for directories
     *
     * @param root The root of the remote file system
     */
    private static void folderLoop(directory root) {

        String line, args[];
        directory current = root;

        do {
            System.out.print(current.name() + "> ");
            line = System.console().readLine();
            args = line.split(" ");

            try {
                if (args[0].equals("ls")) {
                    Command command = new ListFiles(current);
                    command.run(args);
                } else if (args[0].equals("cd")) {
                    FolderCommand openDirectory = new OpenDirectory(root, current);
                    openDirectory.run(args);
                    current = openDirectory.getDirectory();
                } else if (args[0].equals("mkdir")) {
                    FolderCommand createDirectory = new CreateDirectory(current);
                    createDirectory.run(args); // Does not move into the new directory
                } else if (args[0].equals("touch")) {
                    FolderCommand createFile = new CreateFile(current);
                    createFile.run(args);
                } else if (args[0].equals("rm")) {
                    FolderCommand command = new DeleteFile(current);
                    command.run(args);
                } else if (args[0].equals("open")) {
                    OpenFile command = new OpenFile(current);
                    command.run(args);
                    regular_fileLoop(command.getFile());
                } else if (args[0].equals("help")) {
                    System.out.println("ls\t\t\tlist directory contents");
                    System.out.println("cd [directory]\t\topen directory");
                    System.out.println("mkdir directory_name\tmake directory");
                    System.out.println("touch filename\t\tcreate empty file");
                    System.out.println("rm filename\t\tremove directory entries");
                    System.out.println("open filename [mode]\topen a regular file");
                    System.out.println("exit\t\t\texit the program");
                    System.out.println("help\t\t\tdisplay this information");
                } else if (args[0].equals("exit")) {} else {
                    System.out.println(args[0] + " : Unknown command");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } while (!line.equals("exit"));
    }

    /**
     * The loop for directories
     *
     * @param root The root of the remote file system
     */
    private static void regular_fileLoop(regular_file file) {
        String line, args[];

        do {
            System.out.print(file.name() + "$ ");
            line = System.console().readLine();
            args = line.split(" ");

            try {
                if (args[0].equals("seek")) {
                    (new SeekCommand(file)).run(args);
                } else if (args[0].equals("read")) {
                    (new ReadCommand(file)).run(args);
                } else if (args[0].equals("write")) {
                    (new WriteCommand(file)).run(args);
                } else if (args[0].equals("close")) {
                    file.close();
                } else if (args[0].equals("help")) {
                    System.out.println("seek n\t\tchange the access position");
                    System.out.println("read n\t\tread n charaters");
                    System.out.println("write \t\twrite in the file");
                    System.out.println("close \t\tclose the file");
                    System.out.println("help \t\tdisplay this information");
                } else {
                    System.out.println(args[0] + " : Unknown command");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } while (!line.equals("close"));
    }

}
