package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.ModuleConfModel;
import zerr.util.BitSetFunction;

//TODO JEDEC DDR4 SPD
//@Data
@Builder
public final class Module extends Thread {
	
	private HashMap<Integer, Rank> hashRank;
	private Controller controller;
//	private Channel channel;
	private Integer amount;

	public static Module create(ModuleConfModel module, Controller controller) {
		HashMap<Integer, Rank> hash = new HashMap<>();
		for (int i = 0; i < module.getRank().size(); i++)
			for (int j = 0; j < module.getAmount(); j++)
				hash.put(j, Rank.create(module.getRank().get(i)));
		
		Module mod = Module.builder()
				.hashRank(hash)
				.controller(controller)
				.amount(module.getAmount())
//				.channel(Channel.create(module.getChannel()))
				.build();
		mod.start();
		return mod;
	}

	@Override
	public void run() {
		while(true) {
			
			try {
				ChannelRequest request = controller.getQueueRequest().take();
				int rank = BitSetFunction.toInt(request.getBankGroup());
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
