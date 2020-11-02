package zerr.simulator.os;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.Bank;
import zerr.simulator.hardware.BankGroup;
import zerr.simulator.hardware.Cell;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.hardware.Module;
import zerr.simulator.hardware.Rank;

@Data
@Builder
public class VirtualAddress {
	private Integer modulesLenght;
	private Integer rankLenght;
	private Integer bankGroupLenght;
	private Integer bankLenght;
	private Integer rowLenght;
	private Integer columnLenght;

	public static VirtualAddress create(Hardware hwd) {
		int modules = hwd.getController().getHashModule().size();
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

		return VirtualAddress.builder()
				.modulesLenght(modules)
				.rankLenght(rank)
				.bankGroupLenght(bankGroup)
				.bankLenght(bank)
				.rowLenght(row)
				.columnLenght(column)
				.build();
	}
	
//	public Bits getAddressRow(int vAddress) {
//		
//	}
}
