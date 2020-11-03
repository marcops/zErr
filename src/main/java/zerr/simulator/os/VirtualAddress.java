package zerr.simulator.os;

import lombok.Builder;
import zerr.simulator.hardware.Bank;
import zerr.simulator.hardware.BankGroup;
import zerr.simulator.hardware.Cell;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.hardware.Module;
import zerr.simulator.hardware.Rank;

@Builder
public class VirtualAddress {
	private long rowSize;
	private long columnSize;
	private long bankSize;
	private long bankGroupSize;
	private long rankSize;
	private long cellSize;
	private long maxAddress;
	
	public static VirtualAddress create(Hardware hwd) {
		Module mod = hwd.getController().getHashModule().get(0);
		
		int rank = mod.getHashRank().size();
		Rank r = mod.getHashRank().get(0);
		
		BankGroup bg = r.getHashChip().get(0).getHashBankGroup().get(0);
		int bankGroup = r.getHashChip().get(0).getHashBankGroup().size();
		
		Bank b = bg.getHashBank().get(0);
		int bank = bg.getHashBank().size();
		
		Cell c = b.getHashCell().get(0);
		int row = c.getRowLength();
		int column = c.getColumnsLength();

		long cellSize = row*column;
		long bankSize = cellSize * bank;
		long bankGroupSize =  bankSize * bankGroup;
		long rankSize =  bankGroupSize * rank;
		
		long maxAddress = rankSize * mod.getAmount();
		return VirtualAddress.builder()
				.rowSize(row)
				.columnSize(column)
				.cellSize(cellSize)
				.bankSize(bankSize)
				.bankGroupSize(bankGroupSize)
				.rankSize(rankSize)
				.maxAddress(maxAddress)
				.build();
	}

	public long getModule(long vAddress) throws IllegalArgumentException {
		if(vAddress >= maxAddress) throw new IllegalArgumentException("exceed memory address");
		return (int) (vAddress / rankSize);
	}

	public long getRank(long vAddress) throws IllegalArgumentException {
		long rank = vAddress - (getModule(vAddress) *  rankSize);
		return rank / bankGroupSize;
	}

	public long getBankGroup(long vAddress) throws IllegalArgumentException  {
		long bankGroup = vAddress 
				- (getRank(vAddress) *  bankGroupSize)
				- (getModule(vAddress) *  rankSize);
		return bankGroup / bankSize;
	}

	public long getBank(long vAddress) throws IllegalArgumentException  {
		long bank = vAddress 
				- (getBankGroup(vAddress) *  bankSize)
				- (getRank(vAddress) *  bankGroupSize)
				- (getModule(vAddress) *  rankSize);
		return bank / cellSize;
	}

	public long getRow(long vAddress) throws IllegalArgumentException  {
		long row =  vAddress 
				- (getBank(vAddress) *  cellSize)
				- (getBankGroup(vAddress) *  bankSize)
				- (getRank(vAddress) *  bankGroupSize)
				- (getModule(vAddress) *  rankSize);
		return row / columnSize;
	}

	public long getColumn(long vAddress) throws IllegalArgumentException  {
		return vAddress 
				- (getRow(vAddress) *  rowSize)
				- (getBank(vAddress) *  cellSize)
				- (getBankGroup(vAddress) *  bankSize)
				- (getRank(vAddress) *  bankGroupSize)
				- (getModule(vAddress) *  rankSize);
	}
	
//	private long calculate(int size) {
//		Long i = 1L;
//		int myPos = 2;
//		int nBits = 64-myPos;
//		i = ~(i << myPos) ;
//		System.out.println(Long.toUnsignedString(i) + "=" + Long.toBinaryString(i));
//		i = i << nBits;
//		i = i >>> nBits;
//	}
}
