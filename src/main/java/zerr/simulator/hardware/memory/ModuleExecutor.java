package zerr.simulator.hardware.memory;

import lombok.extern.slf4j.Slf4j;
import zerr.util.Bits;

@Slf4j
public class ModuleExecutor extends Thread {
	private Module module;

	public ModuleExecutor(Module module) {
		this.module = module;
	}

	@Override
	public void run() {
		while (true) {
			try {
				ChannelEvent request = module.getChannelBuffer().getIn().take();
				int rank = request.getRank().toInt();
				if (rank == -1) return;
				if (rank < 0 || rank >= module.getHashRank().size()) {
					log.error("FATAL: Wrong rank");
					return;
				}
				Bits bits = module.getHashRank().get(rank).exec(request);
				if (request.getControlSignal().isDataOkToRead()) {
					request.setData(bits);
					module.getChannelBuffer().getOut().add(request);
				}
			} catch (InterruptedException e) {
				log.error("FATAL: Wrong take", e);
				return;
			}
		}
	}
}
