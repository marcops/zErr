package zerr.simulator.os;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;

//import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import zerr.simulator.Util;
import zerr.simulator.hardware.memcontroller.VirtualAddressService;

class VirtualAddressTest {

	@Test
	void validate4Bytes() throws Exception {
		VirtualAddressService vAddress = VirtualAddressService.create(Util.loadConfig("4bytes.json").getController());

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
	void exceed4Bytes() throws Exception {
		VirtualAddressService vAddress = VirtualAddressService.create(Util.loadConfig("4bytes.json").getController());

		long address = 4;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			vAddress.getModule(address);
		});
	}

	@Test
	void testBank() throws Exception {
		VirtualAddressService vAddress = VirtualAddressService.create(Util.loadConfig("4bytes2bank.json").getController());

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(1L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}

	@Test
	void testGroupBank() throws Exception {
		VirtualAddressService vAddress = VirtualAddressService.create(Util.loadConfig("4bytes2Groupbank.json").getController());

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(0L, vAddress.getRank(address));
		assertEquals(1L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}

	@Test
	void testRank() throws Exception {
		VirtualAddressService vAddress = VirtualAddressService.create(Util.loadConfig("4bytes2Rank.json").getController());

		long address = 4;
		assertEquals(0L, vAddress.getModule(address));
		assertEquals(1L, vAddress.getRank(address));
		assertEquals(0L, vAddress.getBankGroup(address));
		assertEquals(0L, vAddress.getBank(address));
		assertEquals(0L, vAddress.getRow(address));
		assertEquals(0L, vAddress.getColumn(address));
	}

}
