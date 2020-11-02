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
	private Integer memorySize;
	
	public static OperationalSystem create(Hardware hwd) {
//		createVirtualAddress(hwd);
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryControlerDriver(MemoryDriver.create(hwd.getController()))
				.build();
	}

//	private static void createVirtualAddress(Hardware hwd) {
//		memorySize =0;
//		Rank r = hwd.getController().getModule().getHashRank().get(0);
//		Chip c = r.getHashChip().get(0);
//		BankGroup bg = c.getHashBankGroup().get(0);
//		Bank b = bg.getHashBank().get(0);
//		Cell cell = b.getHashCell().get(0);
//		cell.getColumnsLenght() * cell.getColumnsLenght();
//			
//		System.out.println(memorySize);
//	}

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
		memoryControlerDriver.writeEvent(address, address, bank, bankGroup, rank, msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		Bits addressTest = Bits.from(vAddress);
		//TODO bank,rank
		Bits bank = Bits.from(0); 
		Bits bankGroup = Bits.from(0);
		Bits rank = Bits.from(0);
		return memoryControlerDriver.readEvent(addressTest,addressTest, bank, bankGroup , rank);
	}

}
