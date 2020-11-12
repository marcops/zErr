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
	private long line; 
	public Gem5Loader(OperationalSystem os) {
		this.os = os;
	}

	// build/X86/gem5.opt --debug-flags=MemoryAccess marco/simple.py | grep -a zErr >  a.txt
	public void exec(String filename) throws InterruptedException {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					loadFile(filename);
				} catch (IOException | URISyntaxException e) {
					log.error("fail on read file ",e);
				}
			}
		};
		thread.start();
		thread.join();
	}

	private void loadFile(String filename) throws IOException, URISyntaxException {
		try (Stream<String> stream = Files.lines(Path.of(
				getClass()
				.getClassLoader()
				.getResource(filename).toURI()))) {
			line=0;
			stream.forEach(x->{
				try {
					readLine(x);
				} catch (Exception  e) {
					log.error("fail on read file ",e);
				}
			});
		}
	}

	private void readLine(String x) throws InterruptedException {
		//7599750: system.mem_ctrl: zErr,RD,28328,0xc1,28000
		String[] splZerr = x.split(",");
		if(splZerr == null) return;
		int length = splZerr.length;
		if (length < 4) return;

		byte value = Integer.valueOf(splZerr[length-2]).byteValue();
		Long address =  Long.valueOf(splZerr[length-3]);
		String read = splZerr[length-4];
		
		line++;
		
		if(read.charAt(0) == 'R') os.read(address);
		else os.write(Bits.from(value), address);
		
		if(line%10000 == 0) log.info("process:" + ((line*100)/2539404) + "%");
	}

}
