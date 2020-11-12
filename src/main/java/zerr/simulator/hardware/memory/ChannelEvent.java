package zerr.simulator.hardware.memory;

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

	@Override
	public String toString() {
		return "ChannelEvent [data=" + data + ", address=" + address + ", rank=" + rank + ", bankGroup=" + bankGroup
				+ ", bank=" + bank + ", module=" + module + ", controlSignal=" + controlSignal + "]";
	}
	
	

}
