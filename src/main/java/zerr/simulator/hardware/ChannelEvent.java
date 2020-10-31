package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;
import zerr.util.Bits;

@Data
@Builder
public class ChannelEvent {
	private Bits data;
	private Bits address;
	private Bits bank;
	private Bits bankGroup;

	private ControlSignal controlSignal;

}
