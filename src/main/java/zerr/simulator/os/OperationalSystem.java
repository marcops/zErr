package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.simulator.hardware.Cell;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
@Slf4j
public class OperationalSystem {
	private Hardware hardware;
	private MemoryControllerDriver memoryDriver;
	private Integer memorySize;
	private VirtualAddress virtualAddress;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryDriver(MemoryControllerDriver.create(hwd.getController()))
				.virtualAddress(VirtualAddress.create(hwd))
				.build();
	}

	public void write(Bits[] msg, int vAddress) {
		for (int i = 0; i < msg.length; i++) write(msg[i], vAddress + i);
	}
	
	public void write(Bits msg, int vAddress) {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		log.info("W-vAddress [" + vAddress + "]" + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod + ", data=" + msg.toLong());
		memoryDriver.writeEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), Bits.from(mod), msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		Bits msg = memoryDriver.readEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), Bits.from(mod));
		log.info("R-vAddress [" + vAddress + "]" + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod + ", data=" + msg.toLong());
		return msg;
	}

	public void shutdown() {
		 memoryDriver.shutdown();		
	}

	public void invertBit(int vAddress, int bitPosition) {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		
		Cell cell = hardware.getController()
			.getHashModule().get((int)mod)
			.getHashRank().get((int)ra)
			.getHashChip().get((int)c)
			.getHashBankGroup().get((int)bg)
			.getHashBank().get((int)b)
			.getHashCell().get(bitPosition);
		int pos =  (int)(r*c);
		cell.getIcell().set(pos, !cell.getIcell().get(pos));
	}

}
