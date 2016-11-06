/**
 * ServerFactory -- Praktikum Experimentierkasten --
 *
 * @author K. Rege
 * @author T. Yoshi
 * @version 1.0 -- Factory zur Erstellung von Server Objekten
 * @version 1.1 -- Logik zum erraten des Binary Names der Klasse eingebaut
 */

package ch.zhaw.ads;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ServerFactory {

	/**
	 * Create CommandExecutor object from binary class name.
	 * @param classBinaryName binary name of the class to load
	 * @returns CommandExecutor object on success, null if class specified
	 *          by classBinaryName is not a CommandExecutor subclass.
	 * @throws ClassNotFoundException if classBinaryName is not in classpath
	 * @throws InstantiationException if an exception is thrown inside of
	 *         the constructor.
	 */
	public CommandExecutor createServer(String classBinaryName)
		throws ClassNotFoundException, InstantiationException {
		Class<?> clazz = Class.forName(classBinaryName);
		if (clazz != null) {
			Object inst;
			try {
				inst = clazz.newInstance();
			} catch (Exception e) {
				throw new InstantiationException("Could not instantiate class");
			}

			if ((inst instanceof CommandExecutor)) {
				return (CommandExecutor)inst;
			} else {
				return null;
			}
		} else {
			String errorMessage = ("There is no class with binary name '"
								   + classBinaryName + "' in the class path.");
			throw new ClassNotFoundException(errorMessage);
		}
	}


	/**
	 * Create CommandExecutor object from a path to a classfile.
	 * @param directory directory in which the classfile resides
	 * @param name filename of the classfile
	 * @returns CommandExecutor object on success, null if the class is
	 *          not in the classpath.
	 * @throws InstantiationException if an exception is thrown inside of
	 *         the constructor.
	 */
	public CommandExecutor createServer(String directory, String name)
		throws InstantiationException {
		final String defaultPackageName = "ch.zhaw.ads";
		String className = name.substring(0, name.indexOf('.'));

		boolean foundClass = false;
		CommandExecutor server = null;

		try {
			String classBinaryName = classBinaryNameOf(defaultPackageName,
													   className);

			server = this.createServer(classBinaryName);
			foundClass = true;
		} catch (ClassNotFoundException e1) {
			System.err.printf("Loading with default package name %s failed%n",
							  defaultPackageName);

			String packageName = guessPackageName(directory, className);

			if (packageName != null) {
				System.err.printf("Guessed package name %s%n", packageName);
				String classBinaryName = classBinaryNameOf(packageName,
														   className);

				try {
					server = this.createServer(classBinaryName);
					foundClass = true;
				} catch (ClassNotFoundException e2) {}
			} else {
				System.err.println("Failed to guess package name. " +
								   "Is the classfile in the classpath?");
			}
		}

		return (foundClass ? server : null);
	}

	private static String classBinaryNameOf(
		String packageName, String className) throws IllegalArgumentException {
		if (className == null || className.length() < 1) {
			throw new IllegalArgumentException("No className given");
		}

		if (packageName == null || packageName.length() < 1) {
			return className;
		}

		return (packageName + "." + className);
	}

	private String guessPackageName(String directory, String className) {
		String[] dirParts = directory.split(File.separator);
		List<String> pcList = Arrays.asList(dirParts);
		List<String> pathComponents = new ArrayList<String>(pcList);

		boolean foundClass = false;
		directory = directory.replaceAll("/+", "/");

		String packageName = "";

		while (!foundClass && pathComponents.size() > 0) {
			int lastPCIndex = (pathComponents.size() - 1);
			String newPrefix = pathComponents.remove(lastPCIndex);

			if (newPrefix == null || newPrefix.length() < 1) { continue; }

			packageName = (packageName.length() > 0
						   ? (newPrefix + "." + packageName)
						   : newPrefix);
			String classBinaryName = classBinaryNameOf(packageName, className);

			try {
				CommandExecutor server = this.createServer(classBinaryName);
				if ((server instanceof CommandExecutor)) {
					foundClass = true;
				}
			} catch (ClassNotFoundException cnfe) {
			} catch (InstantiationException ie) {
				foundClass = true;
			}
		}

		return (foundClass ? packageName : null);
	}
}
