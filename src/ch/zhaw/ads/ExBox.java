/**
 * @author K. Rege
 * @version 1.0 -- Experimentierkasten
 */
package ch.zhaw.ads;

import java.util.HashMap;

public class ExBox {

    public static void main(String[] args) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();

		{
			int i = 0, argc = args.length;
			String key = "";
			for (; i < argc; i++) {
				if (key == null || key.length() < 1) {
					// new argument
					if ("-class".equals(args[i])) {
						key = args[i]; // this is an argument option
						continue;
					} else if ("-file".equals(args[i])) {
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

					if ("-class".equals(key)) {
						parameters.put("class", args[i]);
					} else if ("-file".equals(key)) {
						parameters.put("file", args[i]);
					}
				}
				key = "";
			}
		}

        ExBoxFrame f = new ExBoxFrame();
        f.setVisible(true);

		// Load specified server
		if (parameters.containsKey("class")) {
			String classBinaryName = parameters.get("class");
			System.out.println("Loading server '" + classBinaryName + "'...");
			f.connectCommand(classBinaryName);
		}

		// Load specified testing file
		if (parameters.containsKey("file")) {
			String testingFilePath = parameters.get("file");
			System.out.println("Loading testing file '" + testingFilePath + "'...");
		    f.readFile(testingFilePath);
		}
    }
}
