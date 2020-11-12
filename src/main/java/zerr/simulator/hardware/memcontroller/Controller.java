package zerr.simulator.hardware.memcontroller;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.ControllerConfModel;
import zerr.simulator.Report;
import zerr.simulator.hardware.memory.Cell;
import zerr.simulator.hardware.memory.ChannelEvent;
import zerr.simulator.hardware.memory.ControlSignal;
import zerr.simulator.hardware.memory.Module;
import zerr.util.Bits;

@Builder
@Data
@Slf4j
public final class Controller {
	private HashMap<Integer, Module> hashModule;
	private EccType eccType;
	private ChannelMode channelMode;
	private PhysicalAddressService physicalAddress;
	private ControllerECC controllerEcc;
	
	//TEST PROPOSE ONLY
	public void waitSync() throws InterruptedException {
		boolean finished = false;
		while (!finished) {
			// TODO pensar no futuro como melhorar
			Thread.sleep(250);
			finished = true;
			for (int i = 0; i < hashModule.size(); i++) {
				finished &= hashModule.get(i).getChannelBuffer().getIn().size() == 0;
			}
		}
	}
	
	public static Controller create(ControllerConfModel controller) {
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < controller.getModule().getAmount(); i++)
			hash.put(i, Module.create(controller.getModule()));
		
		Controller c = Controller.builder()
				.hashModule(hash)
				.eccType(controller.getEccType())
				.channelMode(controller.getChannelMode())
				.controllerEcc(ControllerECC.builder().eccType(controller.getEccType()).build())
				.build();
		c.setPhysicalAddress(PhysicalAddressService.create(c));
		return c;
	}

	public void write(Bits msg, long pAddress) {
		Report.getInstance().addWriteInstruction();
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
		log.info("W-" + va.toString()+ ", data=" + msg.toLong());
		this.writeEvent(va, controllerEcc.encode(msg));
	}

	public Bits read(long pAddress) throws InterruptedException {
		Report.getInstance().addReadInstruction();
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
		Bits msg = this.readEvent(va);
		log.info("R-" + va.toString() + ", data=" + msg.toLong());
		return controllerEcc.decode(pAddress, msg);
	}


	public void invertBit(long pAddress, int bitPosition) {
		if(bitPosition<0 || bitPosition >= 64) {
			throw new IllegalArgumentException("bitPosition between 0 and 64");
		}
		
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
		int chipPos = bitPosition/8;
		Cell cell = this
			.getHashModule().get(va.getModule().toInt())
			.getHashRank().get(va.getRank().toInt())
			.getHashChip().get(chipPos)
			.getHashBankGroup().get(va.getBankGroup().toInt())
			.getHashBank().get(va.getBank().toInt())
			.getHashCell().get(bitPosition-(chipPos*8));
		
		log.info("I-" + va + " bitPosition="+bitPosition );
		cell.getIcell().invert(va.getCellPosition());
	}

	
	public void shutdown() {
		for (int i = 0; i < hashModule.size(); i++)
			hashModule.get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build());
	}

	public void writeEvent(PhysicalAddress pAddress, final Bits data) {
		this.sendCommand(ChannelEvent.builder()
				.address(pAddress.getRow())
				.bank(pAddress.getBank())
				.bankGroup(pAddress.getBankGroup())
				.rank(pAddress.getRank())
				.module(pAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.loadRow())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.address(pAddress.getColumn())
				.bank(pAddress.getBank())
				.bankGroup(pAddress.getBankGroup())
				.rank(pAddress.getRank())
				.module(pAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.setSenseAmpColumn())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.bank(pAddress.getBank())
				.bankGroup(pAddress.getBankGroup())
				.rank(pAddress.getRank())
				.module(pAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.writeCell())
				.build());
	}

	private void sendCommand(ChannelEvent command) {
		hashModule.get(command.getModule().toInt()).sendCommand(command);
	}

	public Bits readEvent(PhysicalAddress pAddress) throws InterruptedException {
			this.sendCommand(ChannelEvent.builder()
					.address(pAddress.getRow())
					.bank(pAddress.getBank())
					.bankGroup(pAddress.getBankGroup())
					.rank(pAddress.getRank())
					.module(pAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			this.sendCommand(ChannelEvent.builder()
					.address(pAddress.getColumn())
					.bank(pAddress.getBank())
					.bankGroup(pAddress.getBankGroup())
					.rank(pAddress.getRank())
					.module(pAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return this.sendCommandAndWait(ChannelEvent.builder()
					.bank(pAddress.getBank())
					.bankGroup(pAddress.getBankGroup())
					.rank(pAddress.getRank())
					.module(pAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}

	private ChannelEvent sendCommandAndWait(ChannelEvent command) throws InterruptedException {
		return hashModule.get(command.getModule().toInt()).sendCommandAndWait(command);
	}
}
