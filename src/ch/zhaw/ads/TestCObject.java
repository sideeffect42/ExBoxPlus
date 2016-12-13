package ch.zhaw.ads;

public class TestCObject implements CommandExecutor {

	private static CObject new_CObject(Object s) {
		CObject obj = (CObject)Storage._new("CObject", s);
		return obj;
	}

	static CObject a;
	static CObject e;

	public String execute(String input) {
		run();
		return Storage.getLog();
	}

	private void run() {
		a = new_CObject("A");
		CObject b = new_CObject("B");
		CObject c = new_CObject("C");
		CObject d = new_CObject("D");
		e = new_CObject("E");
		CObject f = new_CObject("F");
		CObject g = new_CObject("G");
		CObject h = new_CObject("H");
		Storage.addRoot(a);
		Storage.addRoot(e);
		a.next = b; b.next = c; b.down = a; c.down = d;
		e.next = f; f.next = g; g.next = e;
		Storage.dump("root", Storage.getRoot());
		Storage.dump("heap1", Storage.getHeap());
		Storage.gc();
		Storage.dump("heap2", Storage.getHeap());
		b.next = f;
		Storage.gc();
		Storage.dump("heap3", Storage.getHeap());
		Storage.gc();
		Storage.dump("heap4", Storage.getHeap());
		Storage.gc();
		Storage.dump("heap5", Storage.getHeap());
	}

}
