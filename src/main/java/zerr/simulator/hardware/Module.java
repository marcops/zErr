package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ModuleConfModel;

@Data
@Builder
public final class Module {
	
	private HashMap<Integer, Rank> hashRank;
	private Integer amount;
	private ChannelBuffer channelBuffer;
	private ModuleExecutor executor;
	
	public static Module create(ModuleConfModel module) {
		//Every chip receive dataSize/chipAmount
		HashMap<Integer, Rank> hash = new HashMap<>();
		for (int j = 0; j < module.getRank().getAmount(); j++)
			hash.put(j, Rank.create(module.getRank()));
		
		Module mod = Module.builder()
				.hashRank(hash)
				.channelBuffer(ChannelBuffer.create(module.getBufferSize()))
				.amount(module.getAmount())
				.build();
		
		mod.setExecutor(new ModuleExecutor(mod));
		mod.getExecutor().start();
		return mod;
	}
	
	public ChannelEvent sendCommand(ChannelEvent e) {
		channelBuffer.getIn().add(e);
		return e;
	}
	
	public ChannelEvent sendCommandAndWait(ChannelEvent e) throws InterruptedException {
		channelBuffer.getIn().add(e);
		return channelBuffer.getOut().take();
	}
}
