/**
 * Unit tests for ReedSolomon
 *
 * Copyright 2015, Backblaze, Inc.  All rights reserved.
 */

package zerr.simulator.hardware.memcontroller.ecc.reed;

import org.junit.jupiter.api.Test;

/**
 * Tests the ReedSolomon class.
 *
 * Most of the test cases were copied from the Python code to make sure that the
 * Java code does the same thing.
 */
public class ReedSolomonTest {

	@Test
	public void testSimple81EncodeDecode() {
		byte[][] dataShards = new byte[][] {
				// WORD 64
				new byte[] { (char) 'A', (char) 'B', (char) 'C', (char) 'D', (char) 'E', (char) 'F', (char) 'G', (char) 'H' },
				new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, };

		boolean[] shardPresent = { true, true };

		ReedSolomon codec = ReedSolomon.create(1, 1);
		codec.encodeParity(dataShards, 0, 8);

		dataShards[1][0] = 0;
		shardPresent[1] = false;

		codec.decodeMissing(dataShards, shardPresent, 0, 8);
		for (int i = 0; i < 8; i++)
			System.out.println((char) dataShards[1][i]);

	}

}
