package zerr.simulator.hardware;

import java.util.BitSet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelRequest {
	private BitSet data;
	private BitSet address;
	private BitSet bank;
	private BitSet bankGroup;

	private boolean ras;
	private boolean cas;
	private boolean we;

}
