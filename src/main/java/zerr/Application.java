package zerr;

import zerr.simulator.Simulator;

class Application {
	public static void main(String []args) throws Exception {
		Simulator.create("64MBEcc.json").run();
	}
}