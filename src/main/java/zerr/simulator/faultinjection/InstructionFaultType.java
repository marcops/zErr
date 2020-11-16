package zerr.simulator.faultinjection;

import java.util.Random;

import zerr.simulator.FaultInjection;
import zerr.simulator.Report;

public class InstructionFaultType implements IFaultType {
	@Override
	public boolean exec(FaultInjection faultInjection) throws Exception {
		long totInst = Report.getInstance().getTotalReadInstruction().get() 
				+ Report.getInstance().getTotalWriteInstruction().get();
		if((totInst % faultInjection.getEvery()) != 0) return false;
		
		
		long pAddress = faultInjection.getMode().exec(faultInjection);
		int bitPosition = new Random().nextInt(8);
		faultInjection.getHardware().getController().invertBit(pAddress, bitPosition);
		return false;
	}

}
