/**
 * ServerFactory -- Praktikum Experimentierkasten --
 *
 * @author K. Rege
 * @version 1.0 -- Factory zur Erstellung von Server Objekten
 */
package ch.zhaw.ads;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Paths;

public class ServerFactory {

    public CommandExecutor createServer(String directory, String name) throws Exception {
		String className = name.substring(0, name.indexOf('.'));
		directory = directory.replaceAll("/+", "/");

		ArrayList<String> pathComponents = new ArrayList(Arrays.asList(directory.toString().split(File.separator)));

		String packageName = "";
		boolean foundClass = false;
		Class clazz = null;

		try {
			clazz = Class.forName("ch.zhaw.ads." + className);
			foundClass = true;
		} catch (ClassNotFoundException e1) {
			while (!foundClass && pathComponents.size() > 0) {
				try {
					String newPrefix = pathComponents.remove((pathComponents.size() - 1));

					if (newPrefix == null || newPrefix.length() < 1) { continue; }

					packageName = (newPrefix + "." + packageName);

					clazz = Class.forName((packageName + className));
					foundClass = true;
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
 		}

		if (!foundClass) {
			return null;
		}

        return (CommandExecutor)clazz.newInstance();
    }
}
