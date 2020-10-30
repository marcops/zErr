package zerr.simulator.os;

import java.util.BitSet;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.BitSetFunction;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private MemoryControlerDriver memoryControlerDriver;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.memoryControlerDriver(MemoryControlerDriver.create(hwd.getHashController()))
				.build();
	}

	//return virtualMemory (notNow)
	//TODO fazer o controle da vmem
	public int write(byte []msg) {
		for (int i = 0; i < msg.length; i++) {
			System.out.println(msg[i]);
			
			//TODO how define it
			BitSet address = BitSetFunction.from("00000000000000000");
			BitSet bank = BitSetFunction.from(0); 
			BitSet bankGroup = BitSetFunction.from(0);
			final BitSet data = BitSetFunction.from(msg[i]);
			
			memoryControlerDriver.writeEvent(0, address, bank, bankGroup, data);
			
		}
		return 0;
	}

	public byte[] read(int address) {
		System.out.println(address);
		return null;
	}

}
