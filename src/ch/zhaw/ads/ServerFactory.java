/**
 * ServerFactory -- Praktikum Experimentierkasten --
 *
 * @author K. Rege
 * @author T. Yoshi
 * @version 1.0 -- Factory zur Erstellung von Server Objekten
 * @version 1.1 -- Logik zum erraten des Binary Names der Klasse eingebaut
 * @version 1.2 -- Auslesen des `package` aus dem Class-File
 */

package ch.zhaw.ads;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ServerFactory {

	private URLClassLoader classLoader;

	public ServerFactory() {
		this.classLoader = this.freshClassLoader(null);
	}

	private URLClassLoader freshClassLoader(URLClassLoader oldLoader) {
		ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = null;
		if (oldLoader != null) {
			urls = oldLoader.getURLs();
		} else {
			urls = new URL[] {};
		}

		return new URLClassLoader(urls, parentLoader);
	}

	/**
	 * Refresh this Factory's classLoader.
	 * Use this to reload a CommandExecutor, by executing this method followed
	 * by a fresh createServer.
	 */
	public void refresh() {
		this.classLoader = this.freshClassLoader(this.classLoader);
	}

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
		Class<?> clazz = Class.forName(classBinaryName, true, this.classLoader);
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
	 *          not in the classpath or not a CommandExecutor.
	 * @throws IOException
	 * @throws InstantiationException if an exception is thrown inside of
	 *         the constructor.
	 */
	public CommandExecutor createServer(String directory, String name)
		throws IOException, InstantiationException {
		final String defaultPackageName = "ch.zhaw.ads";
		String className = name.substring(0, name.indexOf('.'));
		String path = (directory + File.separator + name);

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

			String packageName = this.readPackageNameFromClassFile(path);
			if (packageName == null) {
				// Fallback to guessing
				packageName = this.guessPackageName(directory, className);
			}

			if (packageName != null) {
				System.err.printf("Package name is %s%n", packageName);
				String classBinaryName = classBinaryNameOf(packageName,
														   className);

				try {
					server = this.createServer(classBinaryName);
					foundClass = true;
				} catch (ClassNotFoundException e2) {
					System.err.printf("Could not find class '%s' in classpath%n",
									  classBinaryName);

					String classDir = this.classPathDirForPackageDir(
											directory, packageName);

					this.addToClassPath(classDir);

					try {
						server = this.createServer(classBinaryName);
						foundClass = true;
					} catch (ClassNotFoundException e3) {}
				}
			} else {
				System.err.println("Failed to determine package name. " +
								   "Is either tools.jar or the classfile in " +
								   "the classpath?");
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

	private String classPathDirForPackageDir(String directory,
											 String packageName) {
		if (directory == null || packageName == null
			|| directory.length() < 1 || packageName.length() < 1) {
			return null;
		}

		ArrayList<String> directoryComponents = new ArrayList<String>(
								Arrays.asList(directory.split(File.separator)));
		ArrayList<String> packageComponents = new ArrayList<String>(
								Arrays.asList(packageName.split("\\.")));

		if (packageComponents.size() > directoryComponents.size()) {
			// how can this be possible?
			return null;
		}

		int offset = (directoryComponents.size() - packageComponents.size());
		for (int i = (packageComponents.size() - 1); i >= 0; i--) {
			if (!directoryComponents.get((offset + i))
				.equals(packageComponents.get(i))) {
				return null;
			}

			directoryComponents.remove((offset + i));
			packageComponents.remove(i);
		}

		// ugly hack because Java cannot join Collections with Strings
		return directoryComponents.toString()
			.replaceAll(", ", File.separator)
			.replaceAll("(^\\[)|(\\]$)", "");
	}

	private void addToClassPath(String directory) {
		int numCurrentURLs = 0;
		URL[] urls;
		if ((this.classLoader instanceof URLClassLoader)) {
			URL[] loaderURLs = this.classLoader.getURLs();
			numCurrentURLs = loaderURLs.length;
			urls = new URL[(numCurrentURLs + 1)];

			System.arraycopy((Object)loaderURLs, 0, (Object)urls, 0, loaderURLs.length);
		} else {
			urls = new URL[1];
		}

		try {
			String urlString = ("file://" + directory + "/");
			URL url = new URL(urlString);

			urls[(urls.length - 1)] = url;
		} catch (MalformedURLException mue) {}

		this.classLoader = new URLClassLoader(urls, this.classLoader);
	}

	private String readPackageNameFromClassFile(String path)
		throws IOException {
		File f = new File(path);

		String classPathSlash = null; // not classpath, but path of the class

		try {
			Class<?> ClassFile = Class.forName("com.sun.tools.classfile.ClassFile");
			Method cfRead = ClassFile.getMethod("read", File.class);
			Method cfGetName = ClassFile.getMethod("getName");
			Object cf = cfRead.invoke(null, f);

			if (cf == null || String.class != cfGetName.getReturnType()) {
				return null;
			}

			classPathSlash = (String)cfGetName.invoke(cf);
		} catch (Exception e) {
			return null;
		}

		if (classPathSlash == null) { return null; }

		File classFile = new File(classPathSlash);
		String packageNameSlash = classFile.getParent();

		String packageName = packageNameSlash.replaceAll("/", ".");

		System.out.println("Read package " + packageName);
		return packageName;
	}
}
