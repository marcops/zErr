package zerr.simulator;

import lombok.Data;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;

@Data
public class ReportError {
	private PhysicalAddress address;
	protected long hardError;
	protected long softError;

	public void addHardError() {
		hardError++;
	}

	public void addSoftError() {
		softError++;
	}

	public String toPrint() {
		return "MemoryData [address=" + address + ", hardError=" + hardError + ", softError=" + softError + "]";
	}

	public ReportError(PhysicalAddress phy) {
		address = phy;
	}
}
