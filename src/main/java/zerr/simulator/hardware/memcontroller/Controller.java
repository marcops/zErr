package zerr.simulator.hardware.memcontroller;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ControllerConfModel;
import zerr.exception.HardErrorException;
import zerr.exception.SoftErrorException;
import zerr.simulator.FaultInjection;
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
				finished &= hashModule.get(i).getChannelBuffer().getIn().isEmpty();
			}
		}
	}
	
	public static Controller create(ControllerConfModel controller) {
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < controller.getModule().getAmount(); i++)
			hash.put(i, Module.create(controller.getModule()));
		
		PhysicalAddressService phy = PhysicalAddressService.create(hash.get(0), controller.getChannelMode());
		return Controller.builder()
				.hashModule(hash)
				.channelMode(controller.getChannelMode())
				.physicalAddress(phy)
				.controllerEcc(ControllerECC.create(controller.getEcc()))
				.build();
	}

	public void write(Bits msg, long pAddress) throws Exception {
		Report.getInstance().addWriteInstruction();
		FaultInjection.getInstance().getType().exec(FaultInjection.getInstance());
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
		log.debug("W-" + va.toString()+ ", data=" + msg.toLong());
		//TODO MELHORAR
		if(ConfigurationService.getInstance().getZErrConfModel().getSimulator().getFull())
			this.writeEvent(va, controllerEcc.encode(msg));
		else
			Report.getInstance().getMemory().write(physicalAddress.getPhysicalAddress(pAddress) , controllerEcc.encode(msg));
	}

	public Bits read(long pAddress) throws Exception {
		Report.getInstance().addReadInstruction();
		FaultInjection.getInstance().getType().exec(FaultInjection.getInstance());
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
		//TODO MELHORAR
		
		Bits msg = doRead(va);
		log.debug("R-" + va.toString() + ", data=" + msg.toLong());
		try {
			return controllerEcc.decode(msg);
		} catch (HardErrorException e) {
			Report.getInstance().hardError(va);
			log.debug("HardError", e);
			return new Bits();
		} catch (SoftErrorException e) {
			Report.getInstance().softError(va);
			log.debug("SoftError", e);
			return e.getRecovered();
		}
	}

	private Bits doRead(PhysicalAddress va) throws InterruptedException {
		if(ConfigurationService.getInstance().getZErrConfModel().getSimulator().getFull())
			return this.readEvent(va);
		return Report.getInstance().getMemory().read(va);
	}


	public void invertBit(long pAddress, int bitPosition) {
		if(bitPosition<0 || bitPosition >= Bits.WORD_LENGTH) {
			throw new IllegalArgumentException("bitPosition between 0 and 64");
		}
		
		PhysicalAddress va = physicalAddress.getPhysicalAddress(pAddress);
//		Report.getInstance().getMemory().invertBit(va, bitPosition);
		int chipPos = bitPosition/Bits.ONE_BYTE;
		Cell cell = this
			.getHashModule().get((int)va.getModule())
			.getHashRank().get((int)va.getRank())
			.getHashChip().get(chipPos)
			.getHashBankGroup().get((int)va.getBankGroup())
			.getHashBank().get((int)va.getBank())
			.getHashCell().get(bitPosition-(chipPos*Bits.ONE_BYTE));
		
		log.debug("I-" + va + " bitPosition="+bitPosition );
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
				.address(Bits.from(pAddress.getRow()))
				.bank(Bits.from(pAddress.getBank()))
				.bankGroup(Bits.from(pAddress.getBankGroup()))
				.rank(Bits.from(pAddress.getRank()))
				.module(Bits.from(pAddress.getModule()))
				.data(data)
				.controlSignal(ControlSignal.loadRow())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.address(Bits.from(pAddress.getColumn()))
				.bank(Bits.from(pAddress.getBank()))
				.bankGroup(Bits.from(pAddress.getBankGroup()))
				.rank(Bits.from(pAddress.getRank()))
				.module(Bits.from(pAddress.getModule()))
				.data(data)
				.controlSignal(ControlSignal.setSenseAmpColumn())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.bank(Bits.from(pAddress.getBank()))
				.bankGroup(Bits.from(pAddress.getBankGroup()))
				.rank(Bits.from(pAddress.getRank()))
				.module(Bits.from(pAddress.getModule()))
				.data(data)
				.controlSignal(ControlSignal.writeCell())
				.build());
	}

	private void sendCommand(ChannelEvent command) {
		hashModule.get(command.getModule().toInt()).sendCommand(command);
	}

	public Bits readEvent(PhysicalAddress pAddress) throws InterruptedException {
			this.sendCommand(ChannelEvent.builder()
					.address(Bits.from(pAddress.getRow()))
					.bank(Bits.from(pAddress.getBank()))
					.bankGroup(Bits.from(pAddress.getBankGroup()))
					.rank(Bits.from(pAddress.getRank()))
					.module(Bits.from(pAddress.getModule()))
					.data(new Bits())
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			this.sendCommand(ChannelEvent.builder()
					.address(Bits.from(pAddress.getColumn()))
					.bank(Bits.from(pAddress.getBank()))
					.bankGroup(Bits.from(pAddress.getBankGroup()))
					.rank(Bits.from(pAddress.getRank()))
					.module(Bits.from(pAddress.getModule()))
					.data(new Bits())
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return this.sendCommandAndWait(ChannelEvent.builder()
					.bank(Bits.from(pAddress.getBank()))
					.bankGroup(Bits.from(pAddress.getBankGroup()))
					.rank(Bits.from(pAddress.getRank()))
					.module(Bits.from(pAddress.getModule()))
					.data(new Bits())
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}

	private ChannelEvent sendCommandAndWait(ChannelEvent command) throws InterruptedException {
		return hashModule.get(command.getModule().toInt()).sendCommandAndWait(command);
	}
}
