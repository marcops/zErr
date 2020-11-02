package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private MemoryDriver memoryControlerDriver;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryControlerDriver(MemoryDriver.create(hwd.getController()))
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
		memoryControlerDriver.writeEvent(address, bank, bankGroup, rank, msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		Bits addressTest = Bits.from(vAddress);
		//TODO bank,rank
		Bits bank = Bits.from(0); 
		Bits bankGroup = Bits.from(0);
		Bits rank = Bits.from(0);
		return memoryControlerDriver.readEvent(addressTest, bank, bankGroup , rank);
	}

}
