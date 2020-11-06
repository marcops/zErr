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
	private Address virtualAddress;
	
	public static Controller create(ControllerConfModel controller) {
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < controller.getModule().getAmount(); i++)
			hash.put(i, Module.create(controller.getModule()));
		
		Controller c = Controller.builder()
				.hashModule(hash)
				.eccType(controller.getEccType())
				.channelMode(controller.getChannelMode())
				.build();
		c.setVirtualAddress(Address.create(c));
		return c;
	}

	public void write(Bits msg, int vAddress) {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		log.info("W-vAddress [" + vAddress + "]" + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod + ", data=" + msg.toLong());
		this.writeEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), Bits.from(mod), msg);
	}

	public Bits read(int vAddress) throws InterruptedException {
		long mod = virtualAddress.getModule(vAddress);
		long ra = virtualAddress.getRank(vAddress);
		long bg = virtualAddress.getBankGroup(vAddress);
		long b = virtualAddress.getBank(vAddress);
		long r = virtualAddress.getRow(vAddress);
		long c = virtualAddress.getColumn(vAddress);
		Bits msg = this.readEvent(Bits.from(r), Bits.from(c), Bits.from(b), Bits.from(bg), Bits.from(ra), Bits.from(mod));
		log.info("R-vAddress [" + vAddress + "]" + ", c=" + c + ", r=" + r + ", b=" + b + ", bg=" + bg  + ", rank=" + ra + ", module=" + mod + ", data=" + msg.toLong());
		return msg;
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
		int pos =  (int)(r*c);
		cell.getIcell().set(pos, !cell.getIcell().get(pos));
	}
	
	public void shutdown() {
		for (int i = 0; i < hashModule.size(); i++)
			hashModule.get(i).sendCommand(ChannelEvent.builder()
					.rank(Bits.from(-1))
					.build());
	}

	private void sendCommand(ChannelEvent command) {
		if(eccType == EccType.HAMMING_SECDEC) {
			Bits b = Hamming.encode(command.getData());
			command.setData(b);
		}
		hashModule.get(command.getModule().toInt()).sendCommand(command);
	}

	private ChannelEvent sendCommandAndWait(ChannelEvent command) throws InterruptedException {
		ChannelEvent received = hashModule.get(command.getModule().toInt()).sendCommandAndWait(command);
		if(eccType == EccType.HAMMING_SECDEC) {
			Bits b = Hamming.decode(received.getData());
			received.setData(b);
		}
		return received;
		
	}
	
	public void writeEvent(Bits addressRow, Bits addressCol, Bits bank, Bits bankGroup, Bits rank, Bits mod, final Bits data) {
		this.sendCommand(ChannelEvent.builder()
				.address(addressRow)
				.bank(bank)
				.bankGroup(bankGroup)
				.rank(rank)
				.data(data)
				.module(mod)
				.controlSignal(ControlSignal.loadRow())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.address(addressCol)
				.bank(bank)
				.bankGroup(bankGroup)
				.rank(rank)
				.data(data)
				.module(mod)
				.controlSignal(ControlSignal.setSenseAmpColumn())
				.build());
		
		this.sendCommand(ChannelEvent.builder()
				.bank(bank)
				.bankGroup(bankGroup)
				.rank(rank)
				.data(data)
				.module(mod)
				.controlSignal(ControlSignal.writeCell())
				.build());
	}

	public Bits readEvent(Bits addressRow,Bits addressCol, Bits bank, Bits bankGroup, Bits rank, Bits mod) throws InterruptedException {
			this.sendCommand(ChannelEvent.builder()
					.address(addressRow)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.loadRow())
					.build());
			
			this.sendCommand(ChannelEvent.builder()
					.address(addressCol)
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.loadColumn())
					.build());
			
			return this.sendCommandAndWait(ChannelEvent.builder()
					.bank(bank)
					.bankGroup(bankGroup)
					.rank(rank)
					.data(new Bits())
					.module(mod)
					.controlSignal(ControlSignal.dataOkToRead())
					.build()).getData();
	}
}
