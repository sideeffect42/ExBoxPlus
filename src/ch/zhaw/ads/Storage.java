package ch.zhaw.ads;

import java.util.*;
import java.lang.reflect.*;

public class Storage {
	public static StringBuffer log = new StringBuffer();
	private static List<Collectable> root = new LinkedList<Collectable>();
	private static List<Collectable> heap = new LinkedList<Collectable>();

	/* add root object */
	public static void addRoot(Collectable obj) {
		root.add(obj);
	}

	// create a collectable object of class cls
	public static Collectable _new(String cls, Object arg){
		Collectable obj = null;
		try {
			// create an object and call constructor
			Constructor<?> cst = Class.forName(("ch.zhaw.ads." + cls))
				.getConstructor(new Class<?>[]{arg.getClass()});
			obj = (Collectable)cst.newInstance(new Object[] { arg });
			// add object to heap
			heap.add(obj);
			log.append(("new " + obj.toString() + "\n"));
		} catch (Exception ex) {
			log.append(("error creating object " + cls + "\n"));
		}
		return obj;
	}

	/* remove object from heap */
	public static void delete(Collectable obj) {
		if (heap.remove(obj))
			log.append(("delete " + obj.toString() + "\n"));
		else
			log.append(("error trying to delete not existing object "
						+ obj.toString() + "\n"));
	}

	/* get all root objects */
	public static Iterable<Collectable> getRoot() {
		return new LinkedList<Collectable>(root);
	}

	/* get heap */
	public static Iterable<Collectable> getHeap() {
		return new LinkedList<Collectable>(heap);
	}

	/* get references to collectables of an object */
	public static Iterable<Collectable> getRefs(Collectable obj) {
		// get all fields of an object
		Field[] fields = obj.getClass().getFields();
		List<Collectable> fieldList = new LinkedList<Collectable>();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object o = fields[i].get(obj);
				if (o instanceof Collectable) {
					fieldList.add((Collectable)o);
				}
			} catch (Exception ex) {}
		}
		return fieldList;
	}

	/* dump an iterator */
	public static void dump(String s, Iterable<?> itr) {
		log.append(s);
		for (Object o : itr) {
			log.append((" " + o.toString()));
		}
		log.append("\n");
	}

	public static String getLog() {
		return log.toString();
	}

	private static void mark(Collectable obj) {
		// TODO
	}
	private static void sweep() {
		// TODO
	}
	public static void gc() {
		// TODO
	}
}
