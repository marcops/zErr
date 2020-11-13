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
		final int SLEEP_TIME = 100;

		for (int c = 0; c < 10; c++) {
			log.info("cycle = " + c);
			// escreve uma palavra para testar o dual-channel
			String msg = "Pepa1";
			os.write(Bits.from(msg), 3);
			// inverte 1 bit dele para ver a correção
			os.invertBit(3, 0);
			Thread.sleep(SLEEP_TIME);
			for (int i = 3; i < 3 + msg.length(); i++) {
				log.info("vMem[" + i + "] " + (char) os.read(i).toInt());
			}
			// escrevre 64 bits para testar n. negativo
			os.write(Bits.from(-1), 0);
			log.info("vMem[" + 0 + "] " + os.read(0).toInt());

			// escreve 64 bits para testar varios 1...1.1.1.1.1.1.1
			os.write(Bits.from(Long.MAX_VALUE), 0);
			log.info("vMem[" + 0 + "] " + os.read(0).toLong());

			// testa invertendo dois bits da palavra pepa para ver o que acontece..
			os.write(Bits.from(msg), 0);
			os.invertBit(2, 5);
			os.invertBit(0, 5);
			Thread.sleep(SLEEP_TIME);
			for (int i = 0; i < msg.length(); i++) {
				log.info("vMem[" + i + "] " + (char) os.read(i).toInt());
			}
			Thread.sleep(SLEEP_TIME);
		}
	}

}
