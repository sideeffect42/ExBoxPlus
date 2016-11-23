/**
 * @author K. Rege
 * @author T. Yoshi
 * @version 1.0 -- Experimentierkasten
 * @version 1.1 -- Added support for arguments
 */

package ch.zhaw.ads;

import java.util.HashMap;

public class ExBox {

	private static final String HELP_ARG = "help";
	private static final String INIT_CLASS_ARG = "class";
	private static final String INIT_FILE_LOAD_ARG = "file";
	private static final String INIT_COMMANDS_LOAD_ARG = "in";
	private static final String INIT_EXEC_ARG = "exec";
	private static final String INIT_TEXT_MODE = "xt";
	private static final String INIT_GRAPHICS_MODE = "xg";
	private static final String INIT_CONSOLE_MODE = "nox";

	public static void main(String[] args) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();

		{
			int i, argc = args.length;
			String key = "";
			for (i = 0; i < argc; i++) {
				if (key != null && key.length() > 0) {
					// this is an option argument
					if (args[i].startsWith("-")) {
						// this option has no argument
						System.err.printf("Option '%s' requires an argument, "
										  + "but none was given!%n", key);
						key = "";
					} else {
						if (("-" + INIT_CLASS_ARG).equals(key)) {
							parameters.put(INIT_CLASS_ARG, args[i]);
						} else if (("-" + INIT_FILE_LOAD_ARG).equals(key)) {
							parameters.put(INIT_FILE_LOAD_ARG, args[i]);
						} else if (("-" + INIT_COMMANDS_LOAD_ARG).equals(key)) {
							parameters.put(INIT_COMMANDS_LOAD_ARG, args[i]);
						} else if (("-" + INIT_EXEC_ARG).equals(key)) {
							parameters.put(INIT_EXEC_ARG, args[i]);
						}

						key = "";
						continue;
					}
				}

				// new argument
				if (("-" + HELP_ARG).equals(args[i])) {
					// Print help
					System.out.println("HELP!!");

					// If help is processed, stop application
					System.exit(0);
				} else if (("-" + INIT_CLASS_ARG).equals(args[i])
						   || ("-" + INIT_FILE_LOAD_ARG).equals(args[i])
						   || ("-" + INIT_COMMANDS_LOAD_ARG).equals(args[i])
						   || ("-" + INIT_EXEC_ARG).equals(args[i])) {
					key = args[i]; // this is an argument option
					continue;
				} else if (("-" + INIT_CONSOLE_MODE).equals(args[i])
						   || ("-" + INIT_TEXT_MODE).equals(args[i])
						   || ("-" + INIT_GRAPHICS_MODE).equals(args[i])) {
					if (!parameters.containsKey("mode")) {
						if (("-" + INIT_TEXT_MODE).equals(args[i])) {
							parameters.put("mode", "text");
						} else if (("-" + INIT_GRAPHICS_MODE).equals(args[i])) {
							parameters.put("mode", "graphics");
						} else if (("-" + INIT_CONSOLE_MODE).equals(args[i])) {
							parameters.put("mode", "console");
						}
					} else {
						System.err.printf(
							"Options '-%s', '-%s' and '-%s' are mutally exclusive.%n",
							 INIT_CONSOLE_MODE, INIT_TEXT_MODE,
							 INIT_GRAPHICS_MODE
							);
					}
				} else {
					System.err.printf("Unknown option '%s'.%n", args[i]);
				}

				key = "";
			}
		}

		ExBoxFrame f = new ExBoxFrame();

		// Set mode
		if (parameters.containsKey("mode")) {
			String mode = parameters.get("mode");
			if ("text".equals(mode)) {
				f.setTextView();
			} else if ("graphics".equals(mode)) {
				f.setGraphicView();
			} else if ("console".equals(mode)) {
				System.err.println("Console mode not implemented yet!");
				System.exit(1);
			}
		}

		// Load specified server
		if (parameters.containsKey(INIT_CLASS_ARG)) {
			String classBinaryName = parameters.get(INIT_CLASS_ARG);
			System.out.println("Loading server '" + classBinaryName + "'...");
			f.connectCommand(classBinaryName);
		}

		// Load specified testing file
		if (parameters.containsKey(INIT_FILE_LOAD_ARG)) {
			String testingFilePath = parameters.get(INIT_FILE_LOAD_ARG);
			System.out.println("Loading testing file '" + testingFilePath + "'...");
			f.readFile(testingFilePath);
		}

		// Load commands file
		if (parameters.containsKey(INIT_COMMANDS_LOAD_ARG)) {
			String commandsFilePath = parameters.get(INIT_COMMANDS_LOAD_ARG);
			System.out.println("Loading commands file '" + commandsFilePath + "'...");
			f.processCommadsFile(commandsFilePath);
		}

		// Execute command
		if (parameters.containsKey(INIT_EXEC_ARG)) {
			String commandString = parameters.get(INIT_EXEC_ARG);
			System.out.println("Processing command '" + commandString + "'");
			f.interpret(commandString);
		}

		f.setVisible(true);
	}
}
