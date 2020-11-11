package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import zerr.simulator.hardware.memory.Bank;
import zerr.simulator.hardware.memory.BankGroup;
import zerr.simulator.hardware.memory.Cell;
import zerr.simulator.hardware.memory.Module;
import zerr.simulator.hardware.memory.Rank;

@Builder
public class VirtualAddressService {
	private long rowSize;
	private long columnSize;
	private long bankSize;
	private long bankGroupSize;
	private long rankSize;
	private long cellSize;
	private long maxAddress;
	private ChannelMode mode;

	public long getMaxAddress() {
		return maxAddress;
	}
	
	public long getColumnSize() {
		return columnSize;
	}
	
	public VirtualAddress getVirtualAddress(Long vAddress) {
		return VirtualAddress.create(this, vAddress);
	}
	
	public static VirtualAddressService create(Controller controller) {
		Module mod = controller.getHashModule().get(0);
		int rank = mod.getHashRank().size();
		Rank r = mod.getHashRank().get(0);

		BankGroup bg = r.getHashChip().get(0).getHashBankGroup().get(0);
		int bankGroup = r.getHashChip().get(0).getHashBankGroup().size();

		Bank b = bg.getHashBank().get(0);
		int bank = bg.getHashBank().size();

		Cell c = b.getHashCell().get(0);
		long row = c.getRowLength();
		long column = c.getColumnsLength();

		long cellSize = row * column;
		long bankSize = cellSize * bank;
		long bankGroupSize = bankSize * bankGroup;
		long rankSize = bankGroupSize * rank;

		long maxAddress = rankSize * mod.getAmount();
		return VirtualAddressService.builder()
				.rowSize(row)
				.columnSize(column)
				.cellSize(cellSize)
				.bankSize(bankSize)
				.bankGroupSize(bankGroupSize)
				.rankSize(rankSize)
				.maxAddress(maxAddress)
				.mode(controller.getChannelMode() == null ? ChannelMode.SINGLE : controller.getChannelMode())
				.build();
	}

	public long getModule(long vAddress) {
		if (vAddress >= maxAddress)
			throw new IllegalArgumentException("exceed memory address");
		
		return mode == ChannelMode.SINGLE ? 
				(vAddress / rankSize) : 
				(vAddress % mode.getValue());
		
	}

	
	private long getMultRank(long vAddress) {
		long rank = vAddress;
		return rank / bankGroupSize;
	}
	
	private long getMultBankGroup(long vAddress) {
		long bankGroup = vAddress 
				- (getMultRank(vAddress) * bankGroupSize);
		return bankGroup / bankSize;
	}

	private long getMultBank(long vAddress) {
		long bank = vAddress 
				- (getMultBankGroup(vAddress) * bankSize) 
				- (getMultRank(vAddress) * bankGroupSize);
		return bank / cellSize;
	}

	private long getMultRow(long vAddress) {
		long row = vAddress - (getMultBank(vAddress) * cellSize) 
				- (getMultBankGroup(vAddress) * bankSize)
				- (getMultRank(vAddress) * bankGroupSize) ;
		return row / columnSize;
	}

	public long getMultColumn(long vAddress) {
		return vAddress - (getMultRow(vAddress) * rowSize) 
				- (getMultBank(vAddress) * cellSize)
				- (getMultBankGroup(vAddress) * bankSize) 
				- (getMultRank(vAddress) * bankGroupSize);
	}

	
	private long getSingleRank(long vAddress) {
		long rank = vAddress - (getModule(vAddress) * rankSize);
		return rank / bankGroupSize;
	}
	
	private long getSingleBankGroup(long vAddress) {
		long bankGroup = vAddress 
				- (getSingleRank(vAddress) * bankGroupSize)
				- (getModule(vAddress) * rankSize);
		return bankGroup / bankSize;
	}
	
	private long getSingleBank(long vAddress) {
		long bank = vAddress 
				- (getSingleBankGroup(vAddress) * bankSize) 
				- (getSingleRank(vAddress) * bankGroupSize)
				- (getModule(vAddress) * rankSize);
		return bank / cellSize;
	}

	private long getSingleRow(long vAddress) {
		long row = vAddress - (getSingleBank(vAddress) * cellSize) 
				- (getSingleBankGroup(vAddress) * bankSize)
				- (getSingleRank(vAddress) * bankGroupSize) 
				- (getModule(vAddress) * rankSize);
		return row / columnSize;
	}

	public long getSingleColumn(long vAddress) {
		return vAddress - (getSingleRow(vAddress) * rowSize) 
				- (getSingleBank(vAddress) * cellSize)
				- (getSingleBankGroup(vAddress) * bankSize) 
				- (getSingleRank(vAddress) * bankGroupSize)
				- (getModule(vAddress) * rankSize);
	}
	
	public long getRank(long vAddress) {
		return ChannelMode.SINGLE == mode ? getSingleRank(vAddress) : getMultRank(vAddress/mode.getValue()) ;
	}

	public long getBankGroup(long vAddress) {
		return ChannelMode.SINGLE == mode ? getSingleBankGroup(vAddress) : getMultBankGroup(vAddress/mode.getValue()) ;
	}
	
	public long getBank(long vAddress) {
		return ChannelMode.SINGLE == mode ? getSingleBank(vAddress) : getMultBank(vAddress/mode.getValue()) ;
	}
	
	public long getRow(long vAddress) {
		return ChannelMode.SINGLE == mode ? getSingleRow(vAddress) : getMultRow(vAddress/mode.getValue()) ;
	}

	public long getColumn(long vAddress) {
		return ChannelMode.SINGLE == mode ? getSingleColumn(vAddress) : getMultColumn(vAddress/mode.getValue()) ;
	}

}
