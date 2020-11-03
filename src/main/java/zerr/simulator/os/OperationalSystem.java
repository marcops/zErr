package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private MemoryController memoryDriver;
	private Integer memorySize;
	private VirtualAddress virtualAddress;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryDriver(MemoryController.create(hwd.getController()))
				.virtualAddress(VirtualAddress.create(hwd))
				.build();
	}

	public void write(Bits[] msg, int vAddress) throws InterruptedException {
		for (int i = 0; i < msg.length; i++) write(msg[i], vAddress + i);
	}
	
	public void write(Bits msg, int vAddress) throws InterruptedException {
		
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		System.out.println("vAddress [" + vAddress + "] " + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod);
		memoryDriver.writeEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), (int)mod, msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		return memoryDriver.readEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), (int)mod);
	}

	public void shutdown() throws InterruptedException {
		 memoryDriver.shutdown();		
	}

}
