package zerr.simulator.faultinjection;

import zerr.simulator.FaultInjection;

public interface IFaultMode {
	void exec(FaultInjection faultInjection) throws Exception;
}
