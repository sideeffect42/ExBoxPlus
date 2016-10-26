/**
 * ServerFactory -- Praktikum Experimentierkasten --
 *
 * @author K. Rege
 * @author T. Yoshi
 * @version 1.0 -- Factory zur Erstellung von Server Objekten
 * @version 1.1 -- Logik zum erraten des Binary Names der Klasse eingebaut
 */

package ch.zhaw.ads;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;

public class ServerFactory {

	public CommandExecutor createServer(String classBinaryName) throws Exception {
		Class<?> clazz = Class.forName(classBinaryName);
		if (clazz != null) {
			return (CommandExecutor)clazz.newInstance();
		} else {
			throw new ClassNotFoundException(("There is no class with binary name '" + classBinaryName + "' in the class path."));
		}
	}

	public CommandExecutor createServer(String directory, String name) throws Exception {
		String className = name.substring(0, name.indexOf('.'));
		directory = directory.replaceAll("/+", "/");

		ArrayList<String> pathComponents = new ArrayList<String>(Arrays.asList(directory.toString().split(File.separator)));

		String packageName = "";
		boolean foundClass = false;
		CommandExecutor server = null;

		try {
			server = this.createServer(("ch.zhaw.ads." + className));
			foundClass = true;
		} catch (ClassNotFoundException e1) {
			while (!foundClass && pathComponents.size() > 0) {
				try {
					String newPrefix = pathComponents.remove((pathComponents.size() - 1));

					if (newPrefix == null || newPrefix.length() < 1) { continue; }

					packageName = (newPrefix + "." + packageName);

					String classBinaryName = (packageName + className);
					server = this.createServer(classBinaryName);
					foundClass = true;
				} catch (ClassNotFoundException e) {
					// ignore
				}
			}
		}

		return (foundClass ? server : null);
	}
}
