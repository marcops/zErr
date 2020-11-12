package zerr.simulator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.FaultInjectionConfModel;
import zerr.configuration.model.SimulatorConfModel;
import zerr.simulator.faultinjection.IFaultMode;
import zerr.simulator.hardware.Hardware;

@Builder
@Data
@Slf4j
public class FaultInjection extends Thread {
	private Long everyMilliseconds;
	private Hardware hardware;
	private IFaultMode mode;
	private boolean running;

	public static FaultInjection create(SimulatorConfModel simulator, Hardware hwd) {
		FaultInjectionConfModel conf = simulator.getFaultinjection();
		IFaultMode mode = conf.getMode().getMode();
		return FaultInjection.builder()
				.everyMilliseconds(conf.getEvery())
				.running(mode != null)
				.mode(mode)
				.hardware(hwd)
				.build();
	}

	@Override
	public void run() {
		while (running) {
			try {
				mode.exec(this);
			} catch (Exception e) {
				log.info("running faultInjection", e);
				return;
			}
		}
		log.info("faultInjection shutdown");
	}

	public void shutdown() {
		running = false;
	}

}
