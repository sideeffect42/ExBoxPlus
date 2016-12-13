package ch.zhaw.ads;

import java.util.*;
import java.lang.reflect.*;

/* base class of collectable objects */
public class CObject implements Collectable {
	private boolean mark; // to mark object
	public CObject next, down;
	public String value;

	public CObject(String value) {
		this.value = value;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public boolean isMarked() {
		return this.mark;
	}

	public String toString() {
		return this.value;
	}

	/* finalize object */
	public void finalize() {
		Storage.log.append(("finalize " + this.toString() + "\n"));
	}
}
