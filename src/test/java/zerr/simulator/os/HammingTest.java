package zerr.simulator.os;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import zerr.simulator.hardware.memcontroller.Hamming;
import zerr.util.Bits;

public class HammingTest {
	@Test
	void validateHamming() {
		int[] originalBits = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
				1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 };
		int[] created = Hamming.encode(originalBits);

		int[] received = Hamming.decode(created);

		System.out.println(received);
		assertArrayEquals(originalBits, received);
	}

	@Test
	void validateHammingWithError() {
		int[] originalBits = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
				1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 };
		int[] created = Hamming.encode(originalBits);
		System.out.println(created.length);
		created[3] = created[3] == 1 ? 0 : 1;
		int[] received = Hamming.decode(created);

		System.out.println(received);
		assertArrayEquals(originalBits, received);
	}

	@Test
	void validateHammingWith2Error() {
		int[] originalBits = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
				1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 };
		int[] created = Hamming.encode(originalBits);
		System.out.println(created.length);
		created[3] = created[3] == 1 ? 0 : 1;
		created[6] = created[6] == 1 ? 0 : 1;
		int[] received = Hamming.decode(created);
		assertNull(received);
	}

	@Test
	void validateHammingBits() {
		Bits originalBits = Bits.from(Long.MAX_VALUE);
		Bits created = Hamming.encode(originalBits);

		Bits received = Hamming.decode(created);

		assertEquals(originalBits.toLong(), received.toLong());
	}
	
	
	@Test
	void validateHammingBitsP() {
		String p = "P";
		Bits originalBits = Bits.from(p)[0];
		Bits created = Hamming.encode(originalBits);

		Bits received = Hamming.decode(created);

		assertEquals(originalBits.toLong(), received.toLong());
	}

	@Test
	void validateHammingBitsWithError() {
		Bits originalBits = Bits.from(Long.MAX_VALUE);
		Bits created = Hamming.encode(originalBits);
		created.set(3, !created.get(3));
		Bits received = Hamming.decode(created);

		assertEquals(originalBits.toLong(), received.toLong());
	}

	@Test
	void validateHammingBitsWith2Error() {
		Bits originalBits = Bits.from(Long.MAX_VALUE);
		Bits created = Hamming.encode(originalBits);
		created.set(3, !created.get(3));
		created.set(6, !created.get(6));
		Bits received = Hamming.decode(created);

		assertEquals(0L, received.toLong());
	}

}
