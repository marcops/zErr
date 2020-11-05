package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.ChannelEvent;
import zerr.simulator.hardware.ControlSignal;
import zerr.simulator.hardware.Controller;
import zerr.util.Bits;

@Builder
@Data
public final class MemoryControllerDriver {
	private Controller controller;

	public void writeEvent(Bits addressRow, Bits addressCol, Bits bank, Bits bankGroup, Bits rank, Bits mod, final Bits data) {
			controller.sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.module(mod)
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			controller.sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.module(mod)
					.controlSignal(ControlSignal.setSenseAmpColumn())
					.build());
			
			controller.sendCommand(ChannelEvent.builder()
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.module(mod)
					.controlSignal(ControlSignal.writeCell())
					.build());
	}

	public static MemoryControllerDriver create(Controller controller) {
		return MemoryControllerDriver
				.builder()
				.controller(controller)
				.build();
	}

	public void shutdown() {
		controller.shutdown();
	}
	
	public Bits readEvent(Bits addressRow,Bits addressCol, Bits bank, Bits bankGroup, Bits rank, Bits mod) throws InterruptedException {
			controller.sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			controller.sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return controller.sendCommandAndWait(ChannelEvent.builder()
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}

}
