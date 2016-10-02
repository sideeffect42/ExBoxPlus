/**
 * @author K. Rege
 * @version 1.0 -- Experimentierkasten
 */
package ch.zhaw.ads;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
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
					switch (args[i]) {
					case "-class":
						key = args[i];
						continue;

					default:
						System.err.println(("Unknown argument '" + args[i] + "'."));
					}
				} else {
					// this is an option argument
					if (args[i].startsWith("-")) {
						// this option argument has no option
						System.err.println(("Argument '" + key + "' is an option argument, but no option was given!"));
						break;
					}

					switch (key) {
					case "-class":
						parameters.put("class", args[i]);
						break;
					}
				}
				key = "";
			}
		}

        ExBoxFrame f = new ExBoxFrame();
        f.setVisible(true);

		if (parameters.containsKey("class")) {
			// Load provided server
			f.connectCommand(parameters.get("class"));
		}
    }
}
