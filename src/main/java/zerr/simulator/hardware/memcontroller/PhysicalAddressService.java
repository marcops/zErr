package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import zerr.simulator.hardware.memory.Bank;
import zerr.simulator.hardware.memory.BankGroup;
import zerr.simulator.hardware.memory.Cell;
import zerr.simulator.hardware.memory.Module;
import zerr.simulator.hardware.memory.Rank;

@Builder
public class PhysicalAddressService {
//	private long rowSize;
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
	
	public PhysicalAddress getPhysicalAddress(Long pAddress) {
		return PhysicalAddress.create(this, pAddress);
	}
	
	public static PhysicalAddressService create(Controller controller) {
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
		return PhysicalAddressService.builder()
//				.rowSize(row)
				.columnSize(column)
				.cellSize(cellSize)
				.bankSize(bankSize)
				.bankGroupSize(bankGroupSize)
				.rankSize(rankSize)
				.maxAddress(maxAddress)
				.mode(controller.getChannelMode() == null ? ChannelMode.SINGLE : controller.getChannelMode())
				.build();
	}

	public long getModule(long pAddress) {
		if (pAddress >= maxAddress)
			throw new IllegalArgumentException("exceed memory address");
		
		return mode == ChannelMode.SINGLE ? 
				(pAddress / rankSize) : 
				(pAddress % mode.getValue());
		
	}

	
	private long getMultRank(long pAddress) {
		long rank = pAddress;
		return rank / bankGroupSize;
	}
	
	private long getMultBankGroup(long pAddress) {
		long bankGroup = pAddress 
				- (getMultRank(pAddress) * bankGroupSize);
		return bankGroup / bankSize;
	}

	private long getMultBank(long pAddress) {
		long bank = pAddress 
				- (getMultBankGroup(pAddress) * bankSize) 
				- (getMultRank(pAddress) * bankGroupSize);
		return bank / cellSize;
	}

	private long getMultRow(long pAddress) {
		long row = pAddress - (getMultBank(pAddress) * cellSize) 
				- (getMultBankGroup(pAddress) * bankSize)
				- (getMultRank(pAddress) * bankGroupSize) ;
		return row / columnSize;
	}

	public long getMultColumn(long pAddress) {
		return pAddress - (getMultRow(pAddress) * columnSize) 
				- (getMultBank(pAddress) * cellSize)
				- (getMultBankGroup(pAddress) * bankSize) 
				- (getMultRank(pAddress) * bankGroupSize);
	}

	
	private long getSingleRank(long pAddress) {
		long rank = pAddress - (getModule(pAddress) * rankSize);
		return rank / bankGroupSize;
	}
	
	private long getSingleBankGroup(long pAddress) {
		long bankGroup = pAddress 
				- (getSingleRank(pAddress) * bankGroupSize)
				- (getModule(pAddress) * rankSize);
		return bankGroup / bankSize;
	}
	
	private long getSingleBank(long pAddress) {
		long bank = pAddress 
				- (getSingleBankGroup(pAddress) * bankSize) 
				- (getSingleRank(pAddress) * bankGroupSize)
				- (getModule(pAddress) * rankSize);
		return bank / cellSize;
	}

	private long getSingleRow(long pAddress) {
		long row = pAddress - (getSingleBank(pAddress) * cellSize) 
				- (getSingleBankGroup(pAddress) * bankSize)
				- (getSingleRank(pAddress) * bankGroupSize) 
				- (getModule(pAddress) * rankSize);
		return row / columnSize;
	}

	public long getSingleColumn(long pAddress) {
		return pAddress - (getSingleRow(pAddress) * columnSize) 
				- (getSingleBank(pAddress) * cellSize)
				- (getSingleBankGroup(pAddress) * bankSize) 
				- (getSingleRank(pAddress) * bankGroupSize)
				- (getModule(pAddress) * rankSize);
	}
	
	public long getRank(long pAddress) {
		return ChannelMode.SINGLE == mode ? getSingleRank(pAddress) : getMultRank(pAddress/mode.getValue()) ;
	}

	public long getBankGroup(long pAddress) {
		return ChannelMode.SINGLE == mode ? getSingleBankGroup(pAddress) : getMultBankGroup(pAddress/mode.getValue()) ;
	}
	
	public long getBank(long pAddress) {
		return ChannelMode.SINGLE == mode ? getSingleBank(pAddress) : getMultBank(pAddress/mode.getValue()) ;
	}
	
	public long getRow(long pAddress) {
		return ChannelMode.SINGLE == mode ? getSingleRow(pAddress) : getMultRow(pAddress/mode.getValue()) ;
	}

	public long getColumn(long pAddress) {
		return ChannelMode.SINGLE == mode ? getSingleColumn(pAddress) : getMultColumn(pAddress/mode.getValue()) ;
	}

}
