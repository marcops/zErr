package zerr.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.os.OperationalSystem;
import zerr.util.Bits;

@SuppressWarnings("static-method")
class SimulatorTest {

	@Test
	void oneWordTest() throws Exception {
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesCRC8.json");
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
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		// escreve 64 bits para testar varios 1...1.1.1.1.1.1.1
		os.writeAndSync(Bits.from(Long.MAX_VALUE), 0);
		assertEquals(Long.MAX_VALUE, os.read(0).toLong());
	}

	@Test
	void longMaxInvertLastPositionTest() throws Exception {
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		// escreve 64 bits para testar varios 1...1.1.1.1.1.1.1
		os.writeAndSync(Bits.from(Long.MAX_VALUE), 0);
		os.invertBit(0, 62);
		assertEquals(0, os.read(0).toLong());
	}

	@Test
	void longNegativeTest() throws Exception {
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

//		//escrevre 64 bits para testar n. negativo
		os.writeAndSync(Bits.from(-1), 0);
		assertEquals(-1L, os.read(0).toLong());
	}

	@Test
	void oneWordInvertedCRC8Test() throws Exception {
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesCRC8.json");
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
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesECC.json");
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
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("2mod4bytesECC.json");
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
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load("64MBEcc.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);

		os.writeAndSync(Bits.from("P"), 25805);
		assertEquals('P', (char) os.read(25805).toInt());
	}

	@Test
	void testAllAdressAndPositionDualMemoryTest() throws Exception {
		testFullAddress("MemEccDual.json");
	}

	@Test
	void testAllAdressAndPositionMemoryTest() throws Exception {
		testFullAddress("MemEccSingle.json");
	}

	@Test
	void testAllAdressAndPosition2MemoryTest() throws Exception {
		testFullAddress("2MemEccSingle.json");
	}

	private static void testFullAddress(String file) throws Exception, InterruptedException {
		ZErrConfModel zErrConfiguration = ConfigurationService.getInstance().load(file);
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		OperationalSystem os = OperationalSystem.create(hwd);
		System.out.println(hwd.getController().getPhysicalAddress().getMaxAddress());

		for (long i = 0; i < hwd.getController().getPhysicalAddress().getMaxAddress(); i++) {
			os.writeAndSync(Bits.from("P"), i);
			assertEquals('P', (char) os.read(i).toInt());
		}

	}

}
