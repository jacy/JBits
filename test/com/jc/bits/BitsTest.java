package com.jc.bits;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsTest {

	@Test
	public void testHightBitPosition() {
		assertEquals(Cards.FACE_TWO, Bits.highBitPosition(0b10));
		assertEquals(Cards.FACE_THREE, Bits.highBitPosition(0b100));
		assertEquals(Cards.FACE_ACE, Bits.highBitPosition(0b10000000000000));
	}
	@Test
	public void testClearHightBit() {
		assertEquals(0b0011, Bits.clearHightBit(0b1111, 0b1100));
		assertEquals(0b0000000000000, Bits.clearHightBit(0b1111000000110, 0b1111000000110));
	}

	@Test
	public void testClearHightBits() {
		assertArrayEquals(new int[] { 0b111111100, 0b000111100 }, Bits.clearHightBit(new int[] { 0b111111111, 0b000111100 }, 0b11));
	}

	@Test
	public void testKeepNumbersOfBit1() {
		assertEquals(0b1000000000000, Bits.keepNumbersOfBit1(0b1111000000110, 1));
		assertEquals(0b101000, Bits.keepNumbersOfBit1(0b101010, 2));
		assertEquals(0b101010, Bits.keepNumbersOfBit1(0b101010, 3));
		assertEquals(0b101010, Bits.keepNumbersOfBit1(0b101010, 4));
	}

	@Test
	public void testCountBit1() {
		assertEquals(0, Bits.countBit1(0b0000));
		assertEquals(4, Bits.countBit1(0b1111));
		assertEquals(5, Bits.countBit1(0b00100111100));
		assertEquals(4, Bits.countBit1(0b1111000000));
		try {
			Bits.countBit1(-1);
			fail();
		} catch (RuntimeException e) {
			assertEquals("must be postive", e.getMessage());
		}
	}

}
