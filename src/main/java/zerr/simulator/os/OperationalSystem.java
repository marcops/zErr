package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Hardware;
import zerr.util.Bits;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;
	private Integer memorySize;
	
	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.build();
	}

	public void write(Bits[] msg, int pAddress) {
		for (int i = 0; i < msg.length; i++) write(msg[i], pAddress + i);
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits[] msg, int pAddress) throws InterruptedException {
		write(msg, pAddress);
		hardware.getController().waitSync();
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits msg, int pAddress) throws InterruptedException {
		write(msg, pAddress);
		hardware.getController().waitSync();
	}


	public void write(Bits bits, int pAddress) {
		hardware.getController().write(bits, pAddress);
	}

	public void invertBit(int pAddress, int bitPosition) {
		hardware.getController().invertBit(pAddress, bitPosition);
	}

	public Bits read(int pAddress) throws InterruptedException {
		return hardware.getController().read(pAddress);
	}

	public void shutdown() {
		hardware.getController().shutdown();
	}

}
