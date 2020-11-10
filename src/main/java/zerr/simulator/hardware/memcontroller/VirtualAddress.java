package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import lombok.Data;
import zerr.util.Bits;

@Builder
@Data
public class VirtualAddress {
	private long vAddress;
	private Bits module;
	private Bits rank;
	private Bits bankGroup;
	private Bits bank;
	private Bits row;
	private Bits column;

	public static VirtualAddress create(VirtualAddressService vas, long vAddress) {
		return VirtualAddress.builder().vAddress(vAddress).module(Bits.from(vas.getModule(vAddress)))
				.rank(Bits.from(vas.getRank(vAddress))).bankGroup(Bits.from(vas.getBankGroup(vAddress)))
				.bank(Bits.from(vas.getBank(vAddress))).row(Bits.from(vas.getRow(vAddress)))
				.column(Bits.from(vas.getColumn(vAddress))).build();
	}

	@Override
	public String toString() {
		return "VirtualAddress [" + vAddress + "] [module=" + module.toInt() + ", rank=" + rank.toInt() + ", bankGroup="
				+ bankGroup.toInt() + ", bank=" + bank.toInt() + ", row=" + row.toInt() + ", column=" + column.toInt() + "]";
	}
}
