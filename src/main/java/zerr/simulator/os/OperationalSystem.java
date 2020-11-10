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

	public void write(Bits[] msg, int vAddress) {
		for (int i = 0; i < msg.length; i++) write(msg[i], vAddress + i);
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits[] msg, int vAddress) throws InterruptedException {
		write(msg, vAddress);
		hardware.getController().waitSync();
	}
	//TEST PROPOSE ONLY
	public void writeAndSync(Bits msg, int vAddress) throws InterruptedException {
		write(msg, vAddress);
		hardware.getController().waitSync();
	}


	public void write(Bits bits, int vAddress) {
		hardware.getController().write(bits, vAddress);
	}

	public void invertBit(int vAddress, int bitPosition) {
		hardware.getController().invertBit(vAddress, bitPosition);
	}

	public Bits read(int vAddress) throws InterruptedException {
		return hardware.getController().read(vAddress);
	}

	public void shutdown() {
		hardware.getController().shutdown();
	}

}
