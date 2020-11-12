package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private long memorySize;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.build();
	}

	public void write(Bits[] msg, long pAddress) {
		for (int i = 0; i < msg.length; i++) write(msg[i], pAddress + i);
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits[] msg, long pAddress) throws InterruptedException {
		write(msg, pAddress);
		hardware.getController().waitSync();
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits msg, long pAddress) throws InterruptedException {
		write(msg, pAddress);
		hardware.getController().waitSync();
	}


	public void write(Bits bits, long pAddress) {
		hardware.getController().write(bits, pAddress);
	}

	public void invertBit(int pAddress, int bitPosition) {
		hardware.getController().invertBit(pAddress, bitPosition);
	}

	public Bits read(long pAddress) throws InterruptedException {
		return hardware.getController().read(pAddress);
	}

	public void shutdown() {
		hardware.getController().shutdown();
	}

}
