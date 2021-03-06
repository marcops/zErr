package zerr.simulator.faultinjection;

import java.util.Random;

import zerr.simulator.FaultInjection;

public class RandomTimeFaultType implements IFaultType {

	@Override
	public boolean exec(FaultInjection faultInjection) throws Exception {
		long max = faultInjection.getHardware().getController().getPhysicalAddress().getMaxAddress();
		long pAddress = new Random().nextInt((int)max);
		int bitPosition = new Random().nextInt(8);
		faultInjection.getHardware().getController().invertBit(pAddress, bitPosition);
		
		Thread.sleep(faultInjection.getEvery());
		return true;
	}

}
