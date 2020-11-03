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

	//TODO fazer o controle da vmem
	public void write(Bits[] msg, int vAddress) throws InterruptedException {
		for (int i = 0; i < msg.length; i++) write(msg[i], vAddress + i);
	}
	
	public void write(Bits msg, int vAddress) throws InterruptedException {
		Bits address = Bits.from(vAddress);
		long mod = virtualAddress.getModule(vAddress);
		System.out.println("module=" + mod);
		long ra = virtualAddress.getRank(vAddress);
		System.out.println("rank=" + ra);
		long bg = virtualAddress.getBankGroup(vAddress);
		System.out.println("bg=" + bg);
		long b = virtualAddress.getBank(vAddress);
		System.out.println("b=" + b);
		long r = virtualAddress.getRow(vAddress);
		System.out.println("r=" + r);
		long c = virtualAddress.getColumn(vAddress);
		System.out.println("c=" + c);
//		System.out.println("vAddress["+vAddress+"]="+address.toBitString(64));
		//TODO bank,rank
		Bits bank = Bits.from(0);
		Bits bankGroup = Bits.from(0);
		Bits rank = Bits.from(0);
		memoryDriver.writeEvent(address, address, bank, bankGroup, rank, 0, msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		Bits addressTest = Bits.from(vAddress);
		//TODO bank,rank
		Bits bank = Bits.from(0); 
		Bits bankGroup = Bits.from(0);
		Bits rank = Bits.from(0);
		return memoryDriver.readEvent(addressTest, addressTest, bank, bankGroup, rank, 0);
	}

}
