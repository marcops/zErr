package zerr.simulator.os;

import java.util.BitSet;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import zerr.simulator.hardware.ChannelRequest;
import zerr.simulator.hardware.Controller;

@Builder
@Data
public final class MemoryControlerDriver {
	private Map<Integer, Controller> hashController;
	/*
	 * !RAS, !CAS, !WE
	 * 
	 * 0 1 1 = send row
	 * 0 0 1 = send col
	 * 1 0 1 = data ok
	 * */
	private static final int RAS_POSITION = 0;
	private static final int CAS_POSITION = 1;
	private static final int WE_POSITION = 2;
	private static final boolean[][] WRITE_EVENT = { { false, true, true }, { false, false, true }, { true, false, true } };
	
	public void writeEvent(int pos, BitSet address, BitSet bank, BitSet bankGroup, final BitSet data) {
		for (int i = 0; i < WRITE_EVENT.length; i++) {
			hashController.get(pos).request(ChannelRequest.builder()
					.address(address)
					.bank(bank)
					.bankGroup(bankGroup)
					.data(data)
					.ras(WRITE_EVENT[i][RAS_POSITION])
					.cas(WRITE_EVENT[i][CAS_POSITION])
					.we(WRITE_EVENT[i][WE_POSITION])
					.build());
		}
	}

	public static MemoryControlerDriver create(Map<Integer, Controller> hashController) {
		return MemoryControlerDriver
				.builder()
				.hashController(hashController)
				.build();
	}

}
