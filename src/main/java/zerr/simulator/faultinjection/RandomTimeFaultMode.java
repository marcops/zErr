package zerr.simulator.faultinjection;

import java.util.Random;

import zerr.simulator.FaultInjection;

public class RandomTimeFaultMode implements IFaultMode {

	@Override
	public void exec(FaultInjection faultInjection) throws Exception {
		long max = faultInjection.getHardware().getController().getVirtualAddress().getMaxAddress();
		long vAddress = (long) new Random().nextInt((int) max);
		int bitPosition = new Random().nextInt(8);
		faultInjection.getHardware().getController().invertBit(vAddress, bitPosition);
		
		Thread.sleep(faultInjection.getEveryMilliseconds());
	}

}
