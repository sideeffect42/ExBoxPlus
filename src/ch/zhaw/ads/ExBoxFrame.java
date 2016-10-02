/**
 * @(#)ExBoxFrame.java
 *
 * JFC ExBox application
 *
 * @author K.Rege
 * @author T. Yoshi
 * @version	1.00 2014/2/3
 * @version	1.01 2016/8/2
 * @version 1.10 2016/10/02
 */

package ch.zhaw.ads;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.io.File;


public class ExBoxFrame extends JFrame implements ActionListener, ItemListener {
	private static final double SCALE = 1;
	private String pathtocompiled;
	private JMenuItem connect, open;
	private JButton enter;
	private JTextField arguments;
	private JComboBox history;
	private JTextArea output;
	private JScrollPane scrollPane;
	private CommandExecutor command;

	public static void setFontSize(int size) {
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);
		for (Object key : keys) {
			if (key != null && key.toString().toLowerCase().contains("font")) {
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
					font = font.deriveFont((float) size);
					UIManager.put(key, font);
				}
			}
		}
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu();
		JMenuItem menuFileExit = new JMenuItem();
		menuFile.setText("File");
		menuFileExit.setText("Exit");

		// Add action listener.for the menu	button
		menuFileExit.addActionListener(new ActionListener() {
			public void	actionPerformed(ActionEvent	e) {
				ExBoxFrame.this.windowClosed();
			}
		});

		menuFile.add(menuFileExit);
		menuBar.add(menuFile);

		JMenu menuServer = new JMenu("Server");
		menuBar.add(menuServer);
		connect = new JMenuItem("Connect");
		connect.addActionListener(this);
		menuServer.add(connect);

		open = new JMenuItem("Open...");
		open.addActionListener(this);
		menuFile.insert(open, 0);
		setJMenuBar(menuBar);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		output = new JTextArea();
		scrollPane = new JScrollPane(output);
		add(BorderLayout.CENTER, scrollPane);

		Panel panel = new Panel(new BorderLayout());
		arguments = new JTextField();
		arguments.addActionListener(this);
		panel.add(BorderLayout.CENTER, arguments);
		enter = new JButton("enter");
		enter.addActionListener(this);
		panel.add(BorderLayout.EAST, enter);
		history = new JComboBox();
		history.addItemListener(this);
		panel.add(BorderLayout.SOUTH, history);
		add(BorderLayout.SOUTH, panel);
	}

	/**
	 * The constructor
	 */
	public ExBoxFrame() throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		pathtocompiled = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ").replace(
				"/", File.separator);
		pathtocompiled += "ch\\zhaw\\ads";
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			pathtocompiled = pathtocompiled.substring(1);
		}
		setFontSize((int) (11 * SCALE));
		setSize(new	Dimension((int) (400 * SCALE), (int) (400 * SCALE)));
		setTitle("ExBox");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initMenu();
		initComponents();
	}

	private void error(String s) {
		output.append("\nERROR:" + s + "\n");
	}

	private void interpret(String args) throws Exception {
		if (!arguments.getText().equals(history.getItemAt(0))
				&& !arguments.getText().equals(history.getSelectedItem())) {
			history.insertItemAt(arguments.getText(), 0);
		}
		if (command == null) {
			error("no Server connected");
		} else {
			String res = command.execute(args);
			output.append(res);
		}
	}

	private void setServer(CommandExecutor server) {
		this.command = server;
		this.setTitle(("ExBox connected to " + server.getClass().getCanonicalName()));
	}

	private void connectCommand() throws Exception {
		FileDialog fd = new FileDialog(this, "Connect");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// check if it's a class file
				return name.endsWith(".class");
			}
		});
		fd.setFile("*Server.class");
		fd.setDirectory(pathtocompiled);
		fd.setVisible(true);
		String directory = fd.getDirectory(), name = fd.getFile();
		if (directory != null && name != null) {
			ServerFactory sf = new ServerFactory();
			CommandExecutor server = sf.createServer(directory, name);
			this.setServer(server);
		}
	}

	public void connectCommand(String classBinaryName) throws Exception {
		ServerFactory sf = new ServerFactory();
		CommandExecutor server = sf.createServer(classBinaryName);

		if (server != null) {
			this.setServer(server);
		} else {
			// invalid classBinaryName
			System.err.println(("Could not load class with binary name '" + classBinaryName + "' as server!"));
		}
	}

	private void openFile()  throws Exception {
		FileDialog fd = new FileDialog(this, "Open");
		fd.setVisible(true);
		String name = fd.getFile();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(fd.getDirectory() + "/" + name), "ISO-8859-1"));
		StringBuffer b = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			b.append(line);
			b.append('\n');
		}
		interpret(b.toString());
	}

	public void	itemStateChanged(ItemEvent e) {
		try {
			arguments.setText((String) e.getItem());
			interpret(arguments.getText());
		} catch (Throwable ex) {
			error(ex.toString());
		}
	}

	public void	actionPerformed(ActionEvent	e) {
		try {
			if ((e.getSource() == arguments) || (e.getSource() == enter)) {
				interpret(arguments.getText());
			} else if (e.getSource() == connect) {
				connectCommand();
			} else if (e.getSource() == open) {
				openFile();
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			error(ex.toString());
		}
	}

	/**
	 * Shutdown	procedure when run as an application.
	 */
	protected void windowClosed() {
		System.exit(0);
	}
}
