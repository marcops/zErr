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

//	private static final ControlSignal[] WRITE_EVENT = { 
//			ControlSignal.loadRow(), 
//			ControlSignal.setSenseAmpColumn(),
//			ControlSignal.writeCell() };
//	
//	private static final ControlSignal[] READ_EVENT = { 
//			ControlSignal.loadRow(), 
//			ControlSignal.loadColumn(),
//			ControlSignal.dataOkToRead() };

	public void writeEvent(Bits addressRow, Bits addressCol, Bits bank, Bits bankGroup, Bits rank, int mod, final Bits data) throws InterruptedException {
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.loadRow())
					.build(), false);
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.setSenseAmpColumn())
					.build(), false);
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
//					.address(new Bits())
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(data)
					.controlSignal(ControlSignal.writeCell())
					.build(), false);
	}

	public static MemoryController create(Controller controller) {
		return MemoryController
				.builder()
				.controller(controller)
				.build();
	}

	public void shutdown() throws InterruptedException {
		for (int i = 0; i < controller.getHashModule().size(); i++)
			controller.getHashModule().get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build(), false);
	}
	public Bits readEvent(Bits addressRow,Bits addressCol, Bits bank, Bits bankGroup, Bits rank, int mod) throws InterruptedException {
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.loadRow())
					.build(), false);
			
			controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.loadColumn())
					.build(), false);
			
			return controller.getHashModule().get(mod).sendCommand(ChannelEvent.builder()
//					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.controlSignal(ControlSignal.dataOkToRead())
					.build(), true).getData();
	}

}
