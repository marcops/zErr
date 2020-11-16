package zerr.simulator.faultinjection;

import java.util.Random;

import zerr.simulator.FaultInjection;

public class RandomFaultMode implements IFaultMode {

	@Override
	public long exec(FaultInjection faultInjection) throws Exception {
		long max = faultInjection.getHardware().getController().getPhysicalAddress().getMaxAddress();
		//TODO FIX IT
		return (long)new Random().nextInt((int)max);
	}

}
