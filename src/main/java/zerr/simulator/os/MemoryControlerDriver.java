package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.ChannelEvent;
import zerr.simulator.hardware.ControlSignal;
import zerr.simulator.hardware.Controller;
import zerr.util.Bits;

@Builder
@Data
public final class MemoryControlerDriver {
	private Controller controller;

	private static final ControlSignal[] WRITE_EVENT = { ControlSignal.loadRow(), 
			ControlSignal.setSenseAmpColumn(),
			ControlSignal.writeCell() };

	public void writeEvent(Bits address, Bits bank, Bits bankGroup, final Bits data) throws InterruptedException {
		for (int i = 0; i < WRITE_EVENT.length; i++) {
			controller.getHashModule().get(0).sendCommand(ChannelEvent.builder()
					.address(address)
					.bank(bank)
					.bankGroup(bankGroup)
					.data(data)
					.controlSignal(WRITE_EVENT[i])
					.build(), false);
		}
	}

	public static MemoryControlerDriver create(Controller controller) {
		return MemoryControlerDriver
				.builder()
				.controller(controller)
				.build();
	}

}
