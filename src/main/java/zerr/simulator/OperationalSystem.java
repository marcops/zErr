package zerr.simulator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationalSystem {
	private Hardware hardware;

	public static OperationalSystem create(Hardware hwd) {
		return OperationalSystem.builder()
				.hardware(hwd)
				.build();
	}
	
	//return virtualMemory (notNow)
	//TODO fazer o controle da vmem
	public int write(byte msg[]) {
		for (int i = 0; i < msg.length; i++)
			System.out.println(msg[i]);
		return 0;
	}

	public byte[] read(int address) {
		System.out.println(address);
		return null;
	}

}
