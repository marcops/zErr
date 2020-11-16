package zerr.simulator;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.FaultInjectionConfModel;
import zerr.configuration.model.SimulatorConfModel;
import zerr.simulator.faultinjection.IFaultMode;
import zerr.simulator.faultinjection.IFaultType;
import zerr.simulator.hardware.Hardware;

@Data
@Slf4j
public class FaultInjection extends Thread {
	private Long every;
	private Hardware hardware;
	private IFaultType type;
	private IFaultMode mode ;
	private boolean running;
	private Integer distance;
	private static FaultInjection INSTANCE = null;
	private FaultInjection() {}
	
	public static FaultInjection getInstance() {
		return INSTANCE;
	}
	
	public static FaultInjection create(SimulatorConfModel simulator, Hardware hwd) {
		FaultInjectionConfModel conf = simulator.getFaultinjection();
		IFaultType type = conf.getType().getType();
		IFaultMode mode = conf.getMode().getType().getType();
		
		INSTANCE = new FaultInjection();
		INSTANCE.setDistance(conf.getMode().getDistance());
		INSTANCE.setEvery(conf.getEvery());
		INSTANCE.setRunning(type != null);
		INSTANCE.setType(type);
		INSTANCE.setMode(mode);
		INSTANCE.setHardware(hwd);
		return INSTANCE;
	}

	@Override
	public void run() {
		while (running) {
			try {
				running = type.exec(this);
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
