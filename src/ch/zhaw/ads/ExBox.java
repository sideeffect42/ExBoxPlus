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


    public static void main(String[] args) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();

		{
			int i = 0, argc = args.length;
			String key = "";
			for (; i < argc; i++) {
				if (key == null || key.length() < 1) {
					// new argument
					if (("-" + HELP_ARG).equals(args[i])) {
						// Print help
						System.out.println("HELP!!");

						// If help is processed, stop application
						System.exit(0);
					} else if (("-" + INIT_CLASS_ARG).equals(args[i])) {
						key = args[i]; // this is an argument option
						continue;
					} else if (("-" + INIT_FILE_LOAD_ARG).equals(args[i])) {
						key = args[i]; // this is an argument option
						continue;
					} else if (("-" + INIT_COMMANDS_LOAD_ARG).equals(args[i])) {
						key = args[i]; // this is an argument option
						continue;
					} else {
						System.err.println(("Unknown argument '" + args[i] + "'."));
					}
				} else {
					// this is an option argument
					if (args[i].startsWith("-")) {
						// this option has no argument
						System.err.println(("Option '" + key + "' requires an argument, but none was given!"));
						break;
					}

					if (("-" + INIT_CLASS_ARG).equals(key)) {
						parameters.put(INIT_CLASS_ARG, args[i]);
					} else if (("-" + INIT_FILE_LOAD_ARG).equals(key)) {
						parameters.put(INIT_FILE_LOAD_ARG, args[i]);
					} else if (("-" + INIT_COMMANDS_LOAD_ARG).equals(key)) {
						parameters.put(INIT_COMMANDS_LOAD_ARG, args[i]);
					}
				}
				key = "";
			}
		}

        ExBoxFrame f = new ExBoxFrame();
        f.setVisible(true);

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
    }
}
