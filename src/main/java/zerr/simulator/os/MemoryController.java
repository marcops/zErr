package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.ChannelEvent;
import zerr.simulator.hardware.ControlSignal;
import zerr.simulator.hardware.Controller;
import zerr.util.Bits;

@Builder
@Data
public final class MemoryController {
	private Controller controller;

	public void writeEvent(Bits addressRow, Bits addressCol, Bits bank, Bits bankGroup, Bits rank, int mod, final Bits data) {
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.setSenseAmpColumn())
					.build());
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.writeCell())
					.build());
	}

	public static MemoryController create(Controller controller) {
		return MemoryController
				.builder()
				.controller(controller)
				.build();
	}

	public void shutdown() {
		for (int i = 0; i < controller.getHashModule().size(); i++)
			controller.getHashModule().get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build());
	}
	
	public Bits readEvent(Bits addressRow,Bits addressCol, Bits bank, Bits bankGroup, Bits rank, int mod) throws InterruptedException {
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return controller.getHashModule().get(mod).sendCommandAndWait(ChannelEvent.builder()
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}

}
