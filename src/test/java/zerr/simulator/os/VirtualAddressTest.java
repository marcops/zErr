package zerr.simulator.os;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;

//import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VirtualAddressTest {

	@Test
	public void validate4Bytes() throws Exception {
		VirtualAddress vAddress = VirtualAddress.create(DataFactory.create4bytesHardware());

		long address = 0;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));

		address = 1;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(1L, vAddress.getColumn(address));

		address = 2;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(1L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));

		address = 3;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(1L, vAddress.getRow(address));
		assertEquals(1L, vAddress.getColumn(address));

	}

	@Test
	public void exceed4Bytes() throws Exception {
		VirtualAddress vAddress = VirtualAddress.create(DataFactory.create4bytesHardware());

		long address = 4;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			vAddress.getModule(address);
		});
	}
	
	@Test
	public void testBank() throws Exception {
		VirtualAddress vAddress = VirtualAddress.create(DataFactory.loadConfig("test4bytes2bank.json"));

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(1L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}
	
	@Test
	public void testGroupBank() throws Exception {
		VirtualAddress vAddress = VirtualAddress.create(DataFactory.loadConfig("test4bytes2Groupbank.json"));

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(1L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}
	
	@Test
	public void testRank() throws Exception {
		VirtualAddress vAddress = VirtualAddress.create(DataFactory.loadConfig("test4bytes2Rank.json"));

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(1L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}

}
