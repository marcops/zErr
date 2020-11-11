package zerr.simulator.os;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;

//import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import zerr.simulator.Util;
import zerr.simulator.hardware.memcontroller.PhysicalAddressService;

class PhysicalAddressTest {

	@Test
	void validate4Bytes() throws Exception {
		PhysicalAddressService pAddress = PhysicalAddressService.create(Util.loadConfig("4bytes.json").getController());

		long address = 0;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(0L, pAddress.getRow(address));
		assertEquals(0L, pAddress.getColumn(address));

		address = 1;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(0L, pAddress.getRow(address));
		assertEquals(1L, pAddress.getColumn(address));

		address = 2;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(1L, pAddress.getRow(address));
		assertEquals(0L, pAddress.getColumn(address));

		address = 3;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(1L, pAddress.getRow(address));
		assertEquals(1L, pAddress.getColumn(address));

	}

	@Test
	void exceed4Bytes() throws Exception {
		PhysicalAddressService pAddress = PhysicalAddressService.create(Util.loadConfig("4bytes.json").getController());

		long address = 4;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			pAddress.getModule(address);
		});
	}

	@Test
	void testBank() throws Exception {
		PhysicalAddressService pAddress = PhysicalAddressService.create(Util.loadConfig("4bytes2bank.json").getController());

		long address = 4;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(1L, pAddress.getBank(address));
		assertEquals(0L, pAddress.getRow(address));
		assertEquals(0L, pAddress.getColumn(address));
	}

	@Test
	void testGroupBank() throws Exception {
		PhysicalAddressService pAddress = PhysicalAddressService.create(Util.loadConfig("4bytes2Groupbank.json").getController());

		long address = 4;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(0L, pAddress.getRank(address));
		assertEquals(1L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(0L, pAddress.getRow(address));
		assertEquals(0L, pAddress.getColumn(address));
	}

	@Test
	void testRank() throws Exception {
		PhysicalAddressService pAddress = PhysicalAddressService.create(Util.loadConfig("4bytes2Rank.json").getController());

		long address = 4;
		assertEquals(0L, pAddress.getModule(address));
		assertEquals(1L, pAddress.getRank(address));
		assertEquals(0L, pAddress.getBankGroup(address));
		assertEquals(0L, pAddress.getBank(address));
		assertEquals(0L, pAddress.getRow(address));
		assertEquals(0L, pAddress.getColumn(address));
	}

}
