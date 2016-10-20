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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import javax.swing.*;


public class ExBoxFrame extends JFrame implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	private static final double SCALE = 1;
	private static final String FILE_ENCODING = "ISO-8859-1";
	private String pathtocompiled;
	private JMenuItem connect, open, textView, graphicView;
	private JButton enter;
	private JTextField arguments;
	private JComboBox history;
	private JTextArea output;
	private JScrollPane scrollPane;
	private CommandExecutor command;
	private boolean graphicOn;
	private GraphicPanel graphic;

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
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

		// Add action listener.for the menu button
		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExBoxFrame.this.windowClosed();
			}
		});

		menuFile.add(menuFileExit);
		menuBar.add(menuFile);

		JMenu menuView = new JMenu("View");

		menuBar.add(menuView);
		textView = new JMenuItem("Text");
		textView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		textView.addActionListener(this);
		menuView.add(textView);
		graphicView = new JMenuItem("Graphic");
		graphicView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
		graphicView.addActionListener(this);
		menuView.add(graphicView);

		JMenu menuServer = new JMenu("Server");
		menuBar.add(menuServer);
		connect = new JMenuItem("Connect...");
		connect.addActionListener(this);
		connect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, Event.CTRL_MASK));
		menuServer.add(connect);

		open = new JMenuItem("Open...");
		open.addActionListener(this);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		menuFile.insert(open, 0);
		this.setJMenuBar(menuBar);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Add output TextPanel
		output = new JTextArea();
		scrollPane = new JScrollPane(output);
		add(BorderLayout.CENTER, scrollPane);


		// Create bottom panel
		Panel bottomPanel = new Panel(new BorderLayout());

		// Input field
		arguments = new JTextField();
		arguments.addActionListener(this);
		bottomPanel.add(BorderLayout.CENTER, arguments);

		// Enter button
		enter = new JButton("enter");
		enter.addActionListener(this);
		bottomPanel.add(BorderLayout.EAST, enter);

		// History box
		history = new JComboBox();
		history.addItemListener(this);
		bottomPanel.add(BorderLayout.SOUTH, history);

		add(BorderLayout.SOUTH, bottomPanel);
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
		// pathtocompiled = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ").replace("/", File.separator);
		// pathtocompiled += "ch\\zhaw\\ads";
		// if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
		// pathtocompiled = pathtocompiled.substring(1);
		// }
		setFontSize((int)(11 * SCALE));
		this.setSize(new Dimension((int)(400 * SCALE), (int)(400 * SCALE)));
		this.setTitle("ExBox");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initMenu();
		this.initComponents();
	}

	private void error(String s) {
		output.append("\nERROR:" + s + "\n");
	}

	private void interpret(String args) throws Exception {
		if (command == null) {
			error("no Server connected");
		} else {
			if (!arguments.getText().equals(history.getItemAt(0)) && !arguments.getText().equals(history.getSelectedItem())) {
				history.insertItemAt(arguments.getText(), 0);
			}

			String res = command.execute(args);
			if (graphicOn) {
				graphic.setFigure(res);
			} else {
				output.append(res);
			}
		}
	}

	private void setServer(CommandExecutor server) {
		if (server == null) { return; }
		this.command = server;
		this.setTitle(("ExBox connected to " + server.getClass().getCanonicalName()));
	}

	private void connectCommand() throws Exception {
		FileDialog fd = new FileDialog(this, "Connect server");
		fd.setFilenameFilter(new FilenameFilter() {
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

	private void openFile() throws Exception {
		FileDialog fd = new FileDialog(this, "Open");
		fd.setVisible(true);
		String path = (fd.getDirectory() + File.separator + fd.getFile());
		this.readFile(path);
	}

	public void readFile(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(fis, FILE_ENCODING));
			StringBuffer b = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				b.append(line);
				b.append('\n');
			}
			this.interpret(b.toString());
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public void processCommadsFile(String path) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		FileInputStream fis = new FileInputStream(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(fis, FILE_ENCODING));
			String line;
			while ((line = br.readLine()) != null) {
				// pass line to interpreter
				try {
					this.interpret(line);
				} catch (Exception e) {
					System.err.println("Could not correctly interpret the line:");
					System.err.println(line);
					System.err.println("Exception was:");
					System.err.println(e.getMessage());
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		try {
			this.arguments.setText((String)e.getItem());
			this.interpret(arguments.getText());
		} catch (Throwable ex) {
			this.error(ex.toString());
		}
	}

	public void setGraphicView() {
		if (graphicOn) return;
		remove(scrollPane);
		graphic = new GraphicPanel();
		output.removeNotify();
		add(BorderLayout.CENTER, graphic);
		graphicOn = true;
		validate();
		repaint();
	}

	public void setTextView() {
		if (!graphicOn) return;
		remove(graphic);
		add(BorderLayout.CENTER, scrollPane);
		graphicOn = false;
		validate();
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if ((e.getSource() == arguments) || (e.getSource() == enter)) {
				this.interpret(arguments.getText());
			} else if (e.getSource() == connect) {
				this.connectCommand();
			} else if (e.getSource() == open) {
				this.openFile();
			} else if (e.getSource() == textView) {
				this.setTextView();
			} else if (e.getSource() == graphicView) {
				this.setGraphicView();
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			this.error(ex.toString());
		}
	}

	/**
	 * Shutdown procedure when run as an application.
	 */
	protected void windowClosed() {
		System.exit(0);
	}
}
