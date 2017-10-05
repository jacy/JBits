package com.jc.bits;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TexasTest {

	@Test
	public void combineResultIntoOneArray() {
		int[] cards = CardUtil.makeCards("4D JH 5D 8C QD TD 7H");
		assertArrayEquals(new int[] { 0b00000010000000, 0b00101000011000, 0b00010001000000, 0b00000000000000 }, Texas.combineResult(cards));
	}

	private TexasResult rankTestCard(String cards) {
		int[] suitCards = CardUtil.makeCards(cards);
		return Texas.rank(suitCards);
	}

	private TexasResult describeRank(String cards) {
		TexasResult binaryResult = rankTestCard(cards);
		return Texas.rankDescription(binaryResult);
	}

	@Test
	public void rankHighCardTest() {
		assertEquals(new TexasResult(TexasResult.RANK_HIGHT_CARD, 0b00111011000000), rankTestCard("4D JH 5D 8C QD TD 7H"));
		assertEquals(new TexasResult(TexasResult.RANK_HIGHT_CARD, 0b11000110010000), rankTestCard("8C AD 5H 3S KD 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_HIGHT_CARD, 0b00110010011000), rankTestCard("4C JH 5C 8D QC 2C 3D"));
	}

	@Test
	public void rankOnePairTest() {
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, 0b00000000000100).score(0b01100100000000), rankTestCard("KD 3S 5H 3D 6C QH 9S"));
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, 0b10000000000000).score(0b01000100010000), rankTestCard("AC 2D 5D AS 4H 9D KD"));
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, 0b00000000000100).score(0b01011000000000), rankTestCard("9S JH 5D TS 3C KC 3H"));
	}

	@Test
	public void rankTwoPairTest() {
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, 0b01000000000000).hight2(0b00100000000000).score(0b00010000000000), rankTestCard("QC KD JD QD JC 5C KC"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, 0b00000001000000).hight2(0b00000000100000).score(0b00010000000000), rankTestCard("7H 3H 6C TD 7C JH 6H"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, 0b00010000000000).hight2(0b00000000010000).score(0b00100000000000), rankTestCard("4D 3S 5H JD JC QH 5S"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, 0b10000000000000).hight2(0b00000000010000).score(0b00000100000000), rankTestCard("AC 2D 5D AS 5H 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, 0b00010000000000).hight2(0b00000000010000).score(0b01000000000000), rankTestCard("9S JH 5D JS 5C KC 3D"));
	}

	@Test
	public void rankThreeKindTest() {
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, 0b00100000000000).score(0b01000100000000), rankTestCard("KH 9S 5H QD QC QH 3S"));
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, 0b01000000000000).score(0b10000100000000), rankTestCard("AC KC KD KS 7H 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, 0b00100000000000).score(0b01001000000000), rankTestCard("KS TS QD QS QH 4C 5D"));
	}

	@Test
	public void rankStraightTest() {
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, 0b01111100000000), rankTestCard("KC QS JH TC 9C 4D 3S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, 0b11111000000000), rankTestCard("AC KS QH JC TC 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, 0b01111100000000), rankTestCard("KS QD JS TC 9S 2D 7S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, 0b00000000011111), rankTestCard("5C 4D 3H 2C AD 7H 9S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, 0b00000011111000), rankTestCard("5H 4S JC 8S 7D 6C 3C"));
	}

	@Test
	public void rankFlushTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.SUIT_DIAMONDS, 0b00110000011010, 0b00110000011010), rankTestCard("4D JD 5D JC QD 2D 7H"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.SUIT_DIAMONDS, 0b11000100011000, 0b11000100011000), rankTestCard("8C AD 5D AS KD 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.SUIT_CLUBS, 0b00110000011100, 0b00110000011100), rankTestCard("4C JC 5C 8D QC 3C 7S"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.SUIT_CLUBS, 0b01110000011000, 0b01110000011100), rankTestCard("4C JC 5C QC 3C KC TD"));
	}

	@Test
	public void rankFullHouseTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, 0b00010000000000).hight2(0b00100000000000), rankTestCard("4D JS 5H JD JC QH QS"));
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, 0b10000000000000).hight2(0b01000000000000), rankTestCard("AC AD KD AS KH 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, 0b00010000000000).hight2(0b01000000000000), rankTestCard("3S JH JD JS KH KC 5D"));
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, 0b00100000000000).hight2(0b00001000000000), rankTestCard("TD QH TH TC 6C QD QC"));
	}

	@Test
	public void rankFourKindTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, 0b00100000000000).score(0b10000000000000), rankTestCard("4D AS 5H QD QC QH QS"));
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, 0b01000000000000).score(0b10000000000000), rankTestCard("AC KC KD KS KH 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, 0b00100000000000).score(0b01000000000000), rankTestCard("KS TS QD QS QH QC 5D"));
	}

	@Test
	public void rankStraighFlushTest() {
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.SUIT_CLUBS, 0b01111100000000), rankTestCard("KC QC JC TC 9C 4D AS"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.SUIT_CLUBS, 0b11111000000000), rankTestCard("AC KC QC JC TC 9D 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.SUIT_SPADES, 0b01111100000000), rankTestCard("KS QS JS TS 9S AD 7S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.SUIT_SPADES, 0b00111110000000), rankTestCard("AS QS JS TS 9S 8S AD"));
	}

	@Test
	public void highCardWinTest() {
		TexasResult h1 = rankTestCard("4D JH 5D 8C QD TD 7H");
		TexasResult h2 = rankTestCard("8C AD 5H 3S KD 9D 4D");
		TexasResult h3 = rankTestCard("4C JH 5C 8D QC 2C 3D");
		assertEquals(TexasResult.RANK_HIGHT_CARD, h1.rank);
		assertEquals(TexasResult.RANK_HIGHT_CARD, h2.rank);
		assertEquals(TexasResult.RANK_HIGHT_CARD, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h1.compareTo(h3) > 0);

		h1 = rankTestCard("AD KH TD 8C 6D 5D 3S");
		h2 = rankTestCard("AH KD TS 8D 6C 5C 2S");
		h3 = rankTestCard("AH KC TD 8C 5S 2S 3S");
		TexasResult h4 = rankTestCard("KD TS 8D 6C 5S QS 9D");
		assertEquals(TexasResult.RANK_HIGHT_CARD, h1.rank);
		assertEquals(TexasResult.RANK_HIGHT_CARD, h2.rank);
		assertEquals(TexasResult.RANK_HIGHT_CARD, h3.rank);
		assertEquals(TexasResult.RANK_HIGHT_CARD, h4.rank);
		assertEquals(h2, h1);
		assertTrue(h2.compareTo(h3) > 0);
		assertTrue(h3.compareTo(h4) > 0);
	}

	@Test
	public void pairWinOnPairTest() {
		TexasResult h1 = rankTestCard("KD 3S 5H 3D 6C QH 9S");
		TexasResult h2 = rankTestCard("AC 2D 5D AS 4H 9D KD");
		TexasResult h3 = rankTestCard("9S JH 5D TS 3C KC 3H");
		assertEquals(TexasResult.RANK_ONE_PAIR, h1.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h2.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h1.compareTo(h3) > 0);
	}

	@Test
	public void pairWinOnScoreTest() {
		TexasResult h1 = rankTestCard("KD KS 9H 8D 7C 5H 4S");
		TexasResult h2 = rankTestCard("KD KS 9H 8D TC 5H 4S");
		TexasResult h3 = rankTestCard("KD KS 9H 8D 7C 5H 4S");
		assertEquals(TexasResult.RANK_ONE_PAIR, h1.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h2.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h3.rank);
		assertEquals(h1, h3);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h2.compareTo(h3) > 0);
	}

	@Test
	public void pairWinMultipleWinnerTest() {
		TexasResult h1 = rankTestCard("KD KS 9H 8D 7C 5H 4S");
		TexasResult h2 = rankTestCard("KD KS 9H 8D 7C 3H 4S");
		TexasResult h3 = rankTestCard("KD KS 9H 8D 7C 2H 4S");
		assertEquals(TexasResult.RANK_ONE_PAIR, h1.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h2.rank);
		assertEquals(TexasResult.RANK_ONE_PAIR, h3.rank);
		assertEquals(h1, h2);
		assertEquals(h2, h3);
	}

	@Test
	public void twoPairWinTest() {
		TexasResult h1 = rankTestCard("4D 3S 5H JD JC QH 5S");
		TexasResult h2 = rankTestCard("AC 2D 5D AS 5H 9D 4D");
		TexasResult h3 = rankTestCard("9S JH 5D JS 5C KC 3D");
		assertEquals(TexasResult.RANK_TWO_PAIR, h1.rank);
		assertEquals(TexasResult.RANK_TWO_PAIR, h2.rank);
		assertEquals(TexasResult.RANK_TWO_PAIR, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h3.compareTo(h1) > 0);
	}

	@Test
	public void twoPairWinMultipleWinnersTest() {
		TexasResult h1 = rankTestCard("5C TC 7H KH 5S TS KS");
		TexasResult h2 = rankTestCard("5C TC 7H KH 5S KC TH");
		assertEquals(TexasResult.RANK_TWO_PAIR, h1.rank);
		assertEquals(TexasResult.RANK_TWO_PAIR, h2.rank);
		assertEquals(h1, h2);
	}

	@Test
	public void threeKindTest() {
		TexasResult h1 = rankTestCard("KH 9S 5H QD QC QH 3S");
		TexasResult h2 = rankTestCard("AC KC KD KS 7H 9D 4D");
		TexasResult h3 = rankTestCard("KS TS QD QS QH 4C 5D");
		assertEquals(TexasResult.RANK_THREE_OF_KIND, h1.rank);
		assertEquals(TexasResult.RANK_THREE_OF_KIND, h2.rank);
		assertEquals(TexasResult.RANK_THREE_OF_KIND, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h3.compareTo(h1) > 0);
	}

	@Test
	public void straightWinTest() {
		TexasResult h1 = rankTestCard("KC QS JH TC 9C 4D 3S");
		TexasResult h2 = rankTestCard("AC KS QH JC TC 9D 4D");
		TexasResult h3 = rankTestCard("KS QD JS TC 9S 2D 7S");
		assertEquals(TexasResult.RANK_STRAIGHT, h1.rank);
		assertEquals(TexasResult.RANK_STRAIGHT, h2.rank);
		assertEquals(TexasResult.RANK_STRAIGHT, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h2.compareTo(h3) > 0);
		assertEquals(h1, h3);
	}

	@Test
	public void flushWinTest() {
		TexasResult h1 = rankTestCard("4D JD 5D JC QD 2D 7H");
		TexasResult h2 = rankTestCard("8C AD 5D AS KD 9D 4D");
		TexasResult h3 = rankTestCard("4C JC 5C 8D QC 3C 7S");
		TexasResult h4 = rankTestCard("4C JC 7C 8D QC 5C 7S");
		assertEquals(TexasResult.RANK_FLUSH, h1.rank);
		assertEquals(TexasResult.RANK_FLUSH, h2.rank);
		assertEquals(TexasResult.RANK_FLUSH, h3.rank);
		assertEquals(TexasResult.RANK_FLUSH, h4.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h2.compareTo(h3) > 0);
		assertTrue(h2.compareTo(h4) > 0);
		assertTrue(h3.compareTo(h1) > 0);
		assertTrue(h4.compareTo(h1) > 0);
	}

	@Test
	public void fourKindWinTest() {
		TexasResult h1 = rankTestCard("4D AS 5H QD QC QH QS");
		TexasResult h2 = rankTestCard("AC KC KD KS KH 9D 4D");
		TexasResult h3 = rankTestCard("KS TS QD QS QH QC 5D");
		assertEquals(TexasResult.RANK_FOUR_OF_KIND, h1.rank);
		assertEquals(TexasResult.RANK_FOUR_OF_KIND, h2.rank);
		assertEquals(TexasResult.RANK_FOUR_OF_KIND, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h1.compareTo(h3) > 0);
	}

	@Test
	public void straightFlushWinTest() {
		TexasResult h1 = rankTestCard("KC QC JC TC 9C 4D AS");
		TexasResult h2 = rankTestCard("AC KC QC JC TC 9D 4D");
		TexasResult h3 = rankTestCard("KS QS JS TS 9S AD 7S");
		assertEquals(TexasResult.RANK_STRAIGHT_FLUSH, h1.rank);
		assertEquals(TexasResult.RANK_STRAIGHT_FLUSH, h2.rank);
		assertEquals(TexasResult.RANK_STRAIGHT_FLUSH, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h2.compareTo(h3) > 0);
		assertTrue(h1.compareTo(h3) == 0);
	}

	@Test
	public void fullHouseWinTest() {
		TexasResult h1 = rankTestCard("4D JS 5H JD JC QH QS");
		TexasResult h2 = rankTestCard("AC AD KD AS KH 9D 4D");
		TexasResult h3 = rankTestCard("3S JH JD JS KH KC 5D");
		assertEquals(TexasResult.RANK_FULL_HOUSE, h1.rank);
		assertEquals(TexasResult.RANK_FULL_HOUSE, h2.rank);
		assertEquals(TexasResult.RANK_FULL_HOUSE, h3.rank);
		assertTrue(h2.compareTo(h1) > 0);
		assertTrue(h2.compareTo(h3) > 0);
		assertTrue(h3.compareTo(h1) > 0);
	}

	@Test
	public void fullHouseMultipleWinnersTest() {
		TexasResult h1 = rankTestCard("2H 2C 5H 5S 5C 7C 4D");
		TexasResult h2 = rankTestCard("2H 2C 5H 5S 5D 4D 2D");
		assertEquals(TexasResult.RANK_FULL_HOUSE, h1.rank);
		assertEquals(TexasResult.RANK_FULL_HOUSE, h2.rank);
		assertEquals(h1, h2);
	}

	@Test
	public void faceFromMaskTest() {
		assertEquals(Cards.FACE_TWO, Texas.faceFromMask(0b10));
		assertEquals(Cards.FACE_THREE, Texas.faceFromMask(0b100));
		assertEquals(Cards.FACE_ACE, Texas.faceFromMask(0b10000000000000));
	}

	@Test
	public void describeTwoPairTest() {
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, Cards.FACE_TWO), describeRank("2H 2C"));
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, Cards.FACE_FIVE), describeRank("5H 5C"));
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, Cards.FACE_THREE), describeRank("4D 3S 3H"));
		assertEquals(new TexasResult(TexasResult.RANK_ONE_PAIR, Cards.FACE_NINE), describeRank("AD 4S 9H 8D 9S 2D TS"));
	}

	@Test
	public void describePairTest() {
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, Cards.FACE_ACE).hight2(Cards.FACE_TWO), describeRank("2H 2C AD AS"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, Cards.FACE_ACE).hight2(Cards.FACE_FOUR), describeRank("4H 4C 8D AS AD"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, Cards.FACE_ACE).hight2(Cards.FACE_EIGHT), describeRank("4H 4C 8D AS AD 8S"));
		assertEquals(new TexasResult(TexasResult.RANK_TWO_PAIR, Cards.FACE_ACE).hight2(Cards.FACE_KING), describeRank("KH KC 8D AS AD 8S 7D"));
	}

	@Test
	public void describeThreeKindTest() {
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, Cards.FACE_TWO), describeRank("2H 2C 2D AS"));
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, Cards.FACE_ACE), describeRank("4H 9C AH AS AD"));
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, Cards.FACE_EIGHT), describeRank("4H 8C 8D AS 2D 8S"));
		assertEquals(new TexasResult(TexasResult.RANK_THREE_OF_KIND, Cards.FACE_KING), describeRank("KH KC 6D AS KD 8S 7D"));
	}

	@Test
	public void describeFourKindTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, Cards.FACE_TWO), describeRank("2H 2C 2D 2S"));
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, Cards.FACE_ACE), describeRank("4H AC AH AS AD"));
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, Cards.FACE_EIGHT), describeRank("4H 8C 8D 8H 2D 8S"));
		assertEquals(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, Cards.FACE_KING), describeRank("KH KC KS AS KD 8S 7D"));
	}

	@Test
	public void describeFullHouseTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, Cards.FACE_TWO).hight2(Cards.FACE_FOUR), describeRank("2H 2C 2D 4S 4D"));
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, Cards.FACE_ACE).hight2(Cards.FACE_THREE), describeRank("KD AH AC AS 3S 3D"));
		assertEquals(new TexasResult(TexasResult.RANK_FULL_HOUSE, Cards.FACE_KING).hight2(Cards.FACE_TEN), describeRank("TS TD KH KC KD 8S 8C"));
	}

	@Test
	public void describeStraightTest() {
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_SIX), describeRank("2D 3S 4D 5S 6H"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_FIVE), describeRank("2D 3S 4D 5S AH"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_FIVE), describeRank("2D 3S 4D 5S AH 8D 2S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_SIX), describeRank("2D 3S 4D 5S 6H 8D 2S"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_SEVEN), describeRank("7D 3S 4D 5S 6H 2D AD"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT, Cards.FACE_ACE), describeRank("AD KS QD JS TH 9S 8H"));
	}

	@Test
	public void describeFlushTest() {
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.FACE_ACE).suit(Cards.SUIT_DIAMONDS), describeRank("2D 8D 4D 9D AD"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.FACE_SEVEN).suit(Cards.SUIT_DIAMONDS), describeRank("2D 3D 4D 5D 6S 7D"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.FACE_KING).suit(Cards.SUIT_HEARTS), describeRank("3H 9H TH KH 4H QH 5H"));
		assertEquals(new TexasResult(TexasResult.RANK_FLUSH, Cards.FACE_QUEEN).suit(Cards.SUIT_HEARTS), describeRank("3H 9H TH KS 4H QH 5H"));
	}

	@Test
	public void describeStraightFlushTest() {
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.FACE_SIX).suit(Cards.SUIT_DIAMONDS), describeRank("2D 3D 4D 5D 6D"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.FACE_FIVE).suit(Cards.SUIT_DIAMONDS), describeRank("2D 3D 4D 5D AD"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.FACE_ACE).suit(Cards.SUIT_HEARTS), describeRank("KH JH TH QH AH"));
		assertEquals(new TexasResult(TexasResult.RANK_STRAIGHT_FLUSH, Cards.FACE_KING).suit(Cards.SUIT_HEARTS), describeRank("KH JH TH QH AS 9H 8H"));
	}

}