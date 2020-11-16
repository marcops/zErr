package zerr.simulator.report.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;
import zerr.util.Bits;

@Getter
public final class MemoryMap {
	private HashMap<Long, MemoryData> memory = new HashMap<>();
	private Set<Long> positionWithFail = new HashSet<>();

	public synchronized Bits read(PhysicalAddress phy) {
		MemoryData mem = getMemoryData(phy);
		mem.setAddress(phy);
		mem.addReadAccess();
		return mem.getData();
	}

	public synchronized void write(PhysicalAddress phy, Bits data) {
		MemoryData mem = getMemoryData(phy);
		mem.setAddress(phy);
		mem.addWriteAccess();
		mem.setData(data);
	}

	private MemoryData getMemoryData(PhysicalAddress phy) {
		if (!memory.containsKey(phy.getPAddress()))
			memory.put(phy.getPAddress(), new MemoryData());
		return memory.get(phy.getPAddress());
	}

//	public synchronized void hardError(PhysicalAddress phy) {
//		MemoryData mem = getMemoryData(phy);
//		mem.addHardError();
//		mem.setAddress(phy);
//		positionWithFail.add(phy.getPAddress());
//	}
//
//	public synchronized void softError(PhysicalAddress phy) {
//		MemoryData mem = getMemoryData(phy);
//		mem.addSoftError();
//		mem.setAddress(phy);
//		positionWithFail.add(phy.getPAddress());
//	}

	public synchronized void invertBit(PhysicalAddress phy, int bitPosition) {
		MemoryData mem = getMemoryData(phy);
		mem.setAddress(phy);
		mem.getData().invert(bitPosition);
	}

	public String getPrintFault() {
		return positionWithFail.stream().map(x -> {
			return memory.get(x).toPrint();
		}).collect(Collectors.joining("\r\n"));
	}
}
