package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import lombok.Data;
import zerr.util.Bits;

@Builder
@Data
public class PhysicalAddress {
	private long pAddress;
	private Bits module;
	private Bits rank;
	private Bits bankGroup;
	private Bits bank;
	private Bits row;
	private Bits column;
	
	private int columnLength;

	public static PhysicalAddress create(PhysicalAddressService vas, long pAddress) {
		return PhysicalAddress.builder()
				.pAddress(pAddress)
				.module(Bits.from(vas.getModule(pAddress)))
				.rank(Bits.from(vas.getRank(pAddress)))
				.bankGroup(Bits.from(vas.getBankGroup(pAddress)))
				.bank(Bits.from(vas.getBank(pAddress)))
				.row(Bits.from(vas.getRow(pAddress)))
				.column(Bits.from(vas.getColumn(pAddress)))
				.columnLength((int)vas.getColumnSize())
				.build();
	}

	public int getCellPosition() {
		return (getRow().toInt()*columnLength) + getColumn().toInt();
	}
	
	@Override
	public String toString() {
		return "PhysicalAddress [" + pAddress + "] [module=" + module.toInt() 
			+ ", rank=" + rank.toInt() 
			+ ", bankGroup=" + bankGroup.toInt() 
			+ ", bank=" + bank.toInt() 
			+ ", row=" + row.toInt() 
			+ ", column=" 
			+ column.toInt() + "]";
	}
}
