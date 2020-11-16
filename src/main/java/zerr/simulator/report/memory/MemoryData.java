package zerr.simulator.report.memory;

import lombok.Data;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;
import zerr.util.Bits;

@Data
public class MemoryData {
	private PhysicalAddress address;
	
	private Bits data = new Bits(Bits.WORD_LENGTH);
	
//	protected long hardError;
//	protected long softError;
	protected long writeAccess;
	protected long readAccess;
	
	
	public void addReadAccess() {
		readAccess++;		
	}
	
//	public void addHardError() {
//		hardError++;		
//	}
//	
//	public void addSoftError() {
//		softError++;		
//	}
	
	public void addWriteAccess() {
		writeAccess++;		
	}

	public String toPrint() {
		return "MemoryData [address=" + address + ", data=" + data + ", writeAccess=" + writeAccess + ", readAccess=" + readAccess + "]";
	}
}
