package zerr.simulator.faultinjection;

import zerr.simulator.FaultInjection;

public interface IFaultMode {
	long exec(FaultInjection faultInjection) throws Exception;
}
