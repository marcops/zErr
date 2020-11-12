package zerr.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.os.OperationalSystem;
import zerr.util.Bits;

class SimulatorTest {

	@Test
	void oneWordTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		String msg = "Pepa";
		os.writeAndSync(Bits.from(msg), 0);
		for (int i = 0; i < msg.length(); i++) {
			assertEquals(msg.charAt(i), (char) os.read(i).toInt());
		}
	}

	@Test
	void longMaxTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		// escreve 64 bits para testar varios 1...1.1.1.1.1.1.1
		os.writeAndSync(Bits.from(Long.MAX_VALUE), 0);
		assertEquals(Long.MAX_VALUE, os.read(0).toLong());
	}
	
	@Test
	void longMaxInvertLastPositionTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		// escreve 64 bits para testar varios 1...1.1.1.1.1.1.1
		os.writeAndSync(Bits.from(Long.MAX_VALUE), 0);
		os.invertBit(0, 62);
		assertEquals(0, os.read(0).toLong());
	}

	@Test
	void longNegativeTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

//		//escrevre 64 bits para testar n. negativo
		os.writeAndSync(Bits.from(-1), 0);
		assertEquals(-1L, os.read(0).toLong());
	}

	
	@Test
	void oneWordInvertedCRC8Test() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		String msg = "Pepa";
		os.writeAndSync(Bits.from(msg), 0);
		// inverte 1 bit dele para ver a correção

		os.invertBit(0, 0);
		for (int i = 1; i < msg.length(); i++) {
			assertEquals(msg.charAt(i), (char) os.read(i).toInt());
		}
		assertEquals(0, (char) os.read(0).toInt());
	}
	
	@Test
	void oneWordInvertedHammingTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesECC.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		String msg = "Pepa";
		os.writeAndSync(Bits.from(msg), 0);
		os.invertBit(0, 0);
		for (int i = 0; i < msg.length(); i++) {
			assertEquals(msg.charAt(i), (char) os.read(i).toInt());
		}
	}
	
	@Test
	void oneWord2InvertedHammingTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesECC.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		String msg = "Pepa";
		os.writeAndSync(Bits.from(msg), 0);
		os.invertBit(0, 0);
		os.invertBit(0, 3);
		assertEquals(0, (char) os.read(0).toInt());
		for (int i = 1; i < msg.length(); i++) {
			assertEquals(msg.charAt(i), (char) os.read(i).toInt());
		}
	}
	
	
	@Test
	void largeLoadMemoryTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("64MBEcc.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		testAddress(os, 25805);
	}
	
	
	@Test
	void testAllAdressAndPositionDualMemoryTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("MemEccDual.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);
		System.out.println(hwd.getController().getPhysicalAddress().getMaxAddress());
		
		for (long i = 0; i < hwd.getController().getPhysicalAddress().getMaxAddress(); i++)
			testAddress(os, i);
		
	}
	
	@Test
	void testAllAdressAndPositionMemoryTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("MemEccSingle.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);
		System.out.println(hwd.getController().getPhysicalAddress().getMaxAddress());
		
		for (long i = 0; i < hwd.getController().getPhysicalAddress().getMaxAddress(); i++)
			testAddress(os, i);
		
	}
	
	@Test
	void testAllAdressAndPosition2MemoryTest() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2MemEccSingle.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);
		System.out.println(hwd.getController().getPhysicalAddress().getMaxAddress());
		
		for (long i = 0; i < hwd.getController().getPhysicalAddress().getMaxAddress(); i++)
			testAddress(os, i);
		
	}
	

	private static void testAddress(OperationalSystem os, long address) throws InterruptedException {
		os.writeAndSync(Bits.from("P"), address);
		assertEquals('P', (char) os.read(address).toInt());
	}
	
	
}
