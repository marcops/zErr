package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.ModuleConfModel;

//TODO JEDEC DDR4 SPD
//@Data
@Builder
public final class Module extends Thread {
	
	private HashMap<Integer, Rank> hashRank;
	private Integer amount;
	private ChannelBuffer channelBuffer;
	
	public static Module create(ModuleConfModel module) {
		HashMap<Integer, Rank> hash = new HashMap<>();
		for (int i = 0; i < module.getRank().size(); i++)
			for (int j = 0; j < module.getAmount(); j++)
				hash.put(j, Rank.create(module.getRank().get(i)));
		
		Module mod = Module.builder()
				.hashRank(hash)
				.channelBuffer(ChannelBuffer.create(module.getBufferSize()))
				.amount(module.getAmount())
				.build();
		mod.start();
		return mod;
	}
	
	public ChannelEvent sendCommand(ChannelEvent e, boolean hasAnswer) throws InterruptedException {
		channelBuffer.getIn().add(e);
		if(hasAnswer) return channelBuffer.getOut().take();
		return e;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				ChannelEvent request = channelBuffer.getIn().take();
				int rank = request.getBankGroup().toInt();
				if(rank < 0 || rank >= amount) {
					System.err.println("FATAL: Wrong rank");
					System.exit(-1);
				}
				hashRank.get(rank).exec(request);
			} catch (InterruptedException e) {
				System.err.println(e);
				System.err.println("FATAL: Wrong take");
				System.exit(-1);
			}
			
		}
	}
}
