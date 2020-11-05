package zerr.test;

import lombok.extern.slf4j.Slf4j;
import zerr.simulator.os.OperationalSystem;
import zerr.util.Bits;

@Slf4j
public class HelloWordApp {
	private OperationalSystem os;

	public HelloWordApp(OperationalSystem os) {
		this.os = os;
	}

	public void exec() throws InterruptedException {
		String msg = "Pepa";
		os.write(Bits.from(msg), 0);

		for (int i = 0; i < msg.length(); i++) {
			log.info("vMem[" + i + "] " + (char) os.read(i).toInt());
		}

		os.write(Bits.from(-1), 0);
		log.info("vMem[" + 0 + "] " + os.read(0).toInt());

		os.write(Bits.from(Long.MAX_VALUE), 0);
		log.info("vMem[" + 0 + "] " + os.read(0).toLong());

	}

}
