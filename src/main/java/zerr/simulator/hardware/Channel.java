package zerr.simulator.hardware;

import java.util.BitSet;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ChannelConfModel;

@Data
@Builder
public class Channel {
	private Integer addressSize;
	private Integer dataSize;
	private Integer bankSize;
	private Integer bankSizeGroup;

	private BitSet data;
	private BitSet address;
	private BitSet bank;
	private BitSet bankGroup;

	private boolean ras;
	private boolean cas;
	private boolean we;

	public static Channel create(ChannelConfModel channel) {
		return Channel.builder()
				.address(new BitSet(channel.getAdressSize()))
				.addressSize(channel.getAdressSize())
				.bank(new BitSet(channel.getBankSize()))
				.bankGroup(new BitSet(channel.getBankGroupSize()))
				.bankSize(channel.getBankSize())
				.bankSizeGroup(channel.getBankGroupSize())
				.cas(false)
				.data(new BitSet(channel.getDataSize()))
				.dataSize(channel.getDataSize())
				.ras(false)
				.we(false)
				.build();
	}
}
