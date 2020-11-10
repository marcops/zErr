package zerr.simulator;

import lombok.Builder;
import zerr.simulator.os.OperationalSystem;

@Builder
public class FailInjection extends Thread {
	private OperationalSystem os;
	private boolean running;

	public static FailInjection create(OperationalSystem os) {
		return FailInjection.builder()
				.os(os)
				.running(true)
				.build();
	}

	@Override
	public void run() {
		while (running) {
			os.invertBit(3, 0);
		}
	}

	public void shutdown() {
		running = false;
	}

}
