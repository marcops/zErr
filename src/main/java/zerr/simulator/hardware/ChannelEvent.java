package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;
import zerr.util.Bits;

@Data
@Builder(toBuilder = true)
public class ChannelEvent {
	private Bits data;
	private Bits address;
	
	private Bits rank;
	private Bits bankGroup;
	private Bits bank;
	private Bits module;

	private ControlSignal controlSignal;

}
