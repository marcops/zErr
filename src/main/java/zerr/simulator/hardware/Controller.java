package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ControllerConfModel;
import zerr.util.Bits;

@Builder
@Data
public final class Controller {
	private HashMap<Integer, Module> hashModule;
	private EccType eccType;
	public static Controller create(ControllerConfModel controller) {
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < controller.getModule().getAmount(); i++)
			hash.put(i, Module.create(controller.getModule()));
		
		return Controller.builder()
				.hashModule(hash)
				.eccType(controller.getType())
				.build();
	}

	public void shutdown() {
		for (int i = 0; i < hashModule.size(); i++)
			hashModule.get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build());
	}

	public void sendCommand(ChannelEvent command) {
		if(eccType == EccType.HAMMING_SECDEC) {
			Bits b = Hamming.encode(command.getData());
			command.setData(b);
		}
		hashModule.get(command.getModule().toInt()).sendCommand(command);
	}

	public ChannelEvent sendCommandAndWait(ChannelEvent command) throws InterruptedException {
		ChannelEvent received = hashModule.get(command.getModule().toInt()).sendCommandAndWait(command);
		if(eccType == EccType.HAMMING_SECDEC) {
			Bits b = Hamming.decode(received.getData());
			received.setData(b);
		}
		return received;
		
	}
}
