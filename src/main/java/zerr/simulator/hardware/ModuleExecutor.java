package zerr.simulator.hardware;

import zerr.util.Bits;

//TODO JEDEC DDR4 SPD
public class ModuleExecutor extends Thread {
	
	private Module module;
	
	public ModuleExecutor(Module module) {
		this.module = module;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				ChannelEvent request = module.getChannelBuffer().getIn().take();
				int rank = request.getBankGroup().toInt();
				if(rank < 0 || rank >= module.getAmount()) {
					System.err.println("FATAL: Wrong rank");
					System.exit(-1);
				}
				Bits bits = module.getHashRank().get(rank).exec(request);
				if(request.getControlSignal().isDataOkToRead()) {
					request.setData(bits);
					module.getChannelBuffer().getOut().add(request);
				}
			} catch (InterruptedException e) {
				System.err.println(e);
				System.err.println("FATAL: Wrong take");
				System.exit(-1);
			}
			
		}
	}
}
