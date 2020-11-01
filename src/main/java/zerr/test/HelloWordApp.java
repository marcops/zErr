package zerr.test;

import zerr.simulator.os.OperationalSystem;
import zerr.util.Bits;

public class HelloWordApp {
	private OperationalSystem os;

	public HelloWordApp(OperationalSystem os) {
		this.os = os;
	}

	public void exec() throws InterruptedException {
		for(int i=0;i<10;i++) {
			os.write(Bits.from(String.valueOf(i)), i);
		}
		
		for(int i=0;i<10;i++) {
			System.out.println("vMem["+i+"] "+(char)os.read(i).toInt());
		}
		
		
		os.write(Bits.from("H"), 10);
		os.write(Bits.from("e"), 11);
		os.write(Bits.from("l"), 12);
		os.write(Bits.from("l"), 13);
		os.write(Bits.from("o"), 14);
		
		System.out.println("vMem["+10+"] "+(char)os.read(10).toInt());
		System.out.println("vMem["+11+"] "+(char)os.read(11).toInt());
		System.out.println("vMem["+12+"] "+(char)os.read(12).toInt());
		System.out.println("vMem["+13+"] "+(char)os.read(13).toInt());
		System.out.println("vMem["+14+"] "+(char)os.read(14).toInt());
//		System.out.println((char)os.read(5).toInt());
		

	}

}
