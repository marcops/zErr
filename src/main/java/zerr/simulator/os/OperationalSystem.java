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
