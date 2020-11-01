package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ModuleConfModel;

//TODO JEDEC DDR4 SPD
@Data
@Builder
public final class Module {
	
	private HashMap<Integer, Rank> hashRank;
	private Integer amount;
	private ChannelBuffer channelBuffer;
	private Integer dataSize;
	private ModuleExecutor executor;
	
	public static Module create(ModuleConfModel module) {
		//Every chip receive dataSize/chipAmount
		HashMap<Integer, Rank> hash = new HashMap<>();
		for (int i = 0; i < module.getRank().size(); i++)
			for (int j = 0; j < module.getAmount(); j++)
				hash.put(j, Rank.create(module.getRank().get(i), 
						module.getChannel().getDataSize() / module.getRank().get(i).getChip().get(0).getAmount()));
		
		Module mod = Module.builder()
				.hashRank(hash)
				.channelBuffer(ChannelBuffer.create(module.getBufferSize()))
				.amount(module.getAmount())
				.dataSize(module.getChannel().getDataSize())
				.build();
		mod.setExecutor(new ModuleExecutor(mod));
		mod.getExecutor().start();
		return mod;
	}
	
	public ChannelEvent sendCommand(ChannelEvent e, boolean hasAnswer) throws InterruptedException {
		channelBuffer.getIn().add(e);
		if(hasAnswer) return channelBuffer.getOut().take();
		return e;
	}
}
