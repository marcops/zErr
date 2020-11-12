package zerr.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import zerr.simulator.os.OperationalSystem;
import zerr.util.Bits;

@Slf4j
public class Gem5Loader {
	private OperationalSystem os;

	public Gem5Loader(OperationalSystem os) {
		this.os = os;
	}

	// build/X86/gem5.opt --debug-flags=MemoryAccess marco/simple.py | grep zErr >
	// a.txt
	public void exec(String filename) throws IOException, URISyntaxException {
		try (Stream<String> stream = Files.lines(Path.of(
				getClass()
				.getClassLoader()
				.getResource(filename).toURI()))) {
			
			stream.forEach(x->{
				try {
					String[] splZerr = x.split(",");
					if(splZerr == null) return;
					//7599750: system.mem_ctrl: zErr,RD,28328,0xc1,28000
					int length = splZerr.length;
					if (length < 4) return;
	//				String time = splZerr[length-1];
					String value = splZerr[length-2];
					String address = splZerr[length-3];
					String read = splZerr[length-4];
					exec(Integer.valueOf(value).byteValue(), Long.valueOf(address), read.charAt(0) == 'R');
				} catch (Exception  e) {
					e.printStackTrace();
				}
			});
		}

//			log.info("vMem[" + 0 + "] " + os.read(0).toLong());
//			os.write(Bits.from(msg), 0);
	}

	private void exec(byte value, long address, boolean read) throws InterruptedException {
		if(read) log.info("vMem[" + 0 + "] " + os.read(address).toLong());
		else os.write(Bits.from(value), address);
	}

}
