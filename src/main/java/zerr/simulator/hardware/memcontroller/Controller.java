package zerr.simulator.hardware.memcontroller;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.ControllerConfModel;
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
	private VirtualAddressService virtualAddress;
	private ControllerECC controllerEcc;
	
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
		c.setVirtualAddress(VirtualAddressService.create(c));
		return c;
	}

	public void write(Bits msg, long vAddress) {
		VirtualAddress va = virtualAddress.getVirtualAddress(vAddress);
		log.info("W-" + va.toString()+ ", data=" + msg.toLong());
		this.writeEvent(va, controllerEcc.encode(msg));
	}

	public Bits read(long vAddress) throws InterruptedException {
		VirtualAddress va = virtualAddress.getVirtualAddress(vAddress);
		Bits msg = this.readEvent(va);
		log.info("R-" + va.toString() + ", data=" + msg.toLong());
		return controllerEcc.decode(msg);
	}


	public void invertBit(int vAddress, int bitPosition) {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		
		Cell cell = this
			.getHashModule().get((int)mod)
			.getHashRank().get((int)ra)
			.getHashChip().get((int)c)
			.getHashBankGroup().get((int)bg)
			.getHashBank().get((int)b)
			.getHashCell().get(bitPosition);
		
		log.info("I-vAddress [" + vAddress + "]" + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod );
		int pos =  (int)((r*cell.getColumnsLength()) + c);
		cell.getIcell().invert(pos);
	}
	
	public void shutdown() {
		for (int i = 0; i < hashModule.size(); i++)
			hashModule.get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build());
	}

	public void writeEvent(VirtualAddress vAddress, final Bits data) {
		this.sendCommand(ChannelEvent.builder()
				.address(vAddress.getRow())
				.bank(vAddress.getBank())
				.bankGroup(vAddress.getBankGroup())
				.rank(vAddress.getRank())
				.module(vAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.loadRow())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.address(vAddress.getColumn())
				.bank(vAddress.getBank())
				.bankGroup(vAddress.getBankGroup())
				.rank(vAddress.getRank())
				.module(vAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.setSenseAmpColumn())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.bank(vAddress.getBank())
				.bankGroup(vAddress.getBankGroup())
				.rank(vAddress.getRank())
				.module(vAddress.getModule())
				.data(data)
				.controlSignal(ControlSignal.writeCell())
				.build());
	}

	private void sendCommand(ChannelEvent command) {
		hashModule.get(command.getModule().toInt()).sendCommand(command);
	}

	public Bits readEvent(VirtualAddress vAddress) throws InterruptedException {
			this.sendCommand(ChannelEvent.builder()
					.address(vAddress.getRow())
					.bank(vAddress.getBank())
					.bankGroup(vAddress.getBankGroup())
					.rank(vAddress.getRank())
					.module(vAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			this.sendCommand(ChannelEvent.builder()
					.address(vAddress.getColumn())
					.bank(vAddress.getBank())
					.bankGroup(vAddress.getBankGroup())
					.rank(vAddress.getRank())
					.module(vAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return this.sendCommandAndWait(ChannelEvent.builder()
					.bank(vAddress.getBank())
					.bankGroup(vAddress.getBankGroup())
					.rank(vAddress.getRank())
					.module(vAddress.getModule())
					.data(new Bits())
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}

	private ChannelEvent sendCommandAndWait(ChannelEvent command) throws InterruptedException {
		return hashModule.get(command.getModule().toInt()).sendCommandAndWait(command);
	}
}
