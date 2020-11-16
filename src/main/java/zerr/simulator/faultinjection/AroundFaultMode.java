package zerr.simulator.faultinjection;

import java.util.Random;

import zerr.simulator.FaultInjection;

public class AroundFaultMode implements IFaultMode {

	private long lastPosition = -1;
	@Override
	public long exec(FaultInjection faultInjection) throws Exception {
		Integer max = (int)faultInjection.getHardware().getController().getPhysicalAddress().getMaxAddress();
		if(lastPosition<0) return new Random().nextInt(max);
		//TODO FIX IT
		Integer dist = faultInjection.getDistance();
		Integer size = max/(dist*100);
		Integer nPos = new Random().nextInt(size*2);
		lastPosition = (lastPosition-size) + nPos;
		return lastPosition;
	}

}
