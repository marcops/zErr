package zerr.test;

import zerr.simulator.os.OperationalSystem;

public class HelloWordApp {
	private OperationalSystem os;

	public HelloWordApp(OperationalSystem os) {
		this.os = os;
	}

	public void exec() {
		int address = os.write("Hello Word".getBytes());
		System.out.println(os.read(address));
	}

}
