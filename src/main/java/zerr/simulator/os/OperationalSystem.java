package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private MemoryControlerDriver memoryControlerDriver;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryControlerDriver(MemoryControlerDriver.create(hwd.getController()))
				.build();
	}

	//return virtualMemory (notNow)
	//TODO fazer o controle da vmem
	public int write(byte []msg) throws InterruptedException {
		for (int i = 0; i < msg.length; i++) {
			System.out.println(msg[i]);
			
			//TODO how define it
			Bits address = Bits.from("00000000000000000");
			Bits bank = Bits.from(0); 
			Bits bankGroup = Bits.from(0);
			final Bits data = Bits.from(msg[i]);
			
			memoryControlerDriver.writeEvent(address, bank, bankGroup, data);
			
		}
		return 0;
	}

	public byte[] read(int address) {
		System.out.println(address);
		return null;
	}

}
