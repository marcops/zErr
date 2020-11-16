package zerr.simulator.faultinjection;

import zerr.simulator.FaultInjection;

public interface IFaultType {
	boolean exec(FaultInjection faultInjection) throws Exception;
}
