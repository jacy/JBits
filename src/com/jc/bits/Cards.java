package com.jc.bits;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

public class Cards {
	public static int[] shuffle(int[] cards) {
		Random random = new Random(System.currentTimeMillis());
		return IntStream.of(cards).mapToObj(card -> new CardMap(random.nextDouble(), card)).sorted(Comparator.comparing(CardMap::getKey)).mapToInt(x -> x.getValue()).toArray();
	}

	public static int[] makeDeck() {
		return IntStream.of(FACES).flatMap(face -> IntStream.of(SUITS).map(suit -> makeCard(face, suit))).toArray();
	}

	public static int makeCard(int face, int suit) {
		return (face << 4) | suit; // 4bits for the suit
	}

	public static final int FACE_ACE = 13;
	public static final int FACE_KING = 12;
	public static final int FACE_QUEEN = 11;
	public static final int FACE_JACK = 10;
	public static final int FACE_TEN = 9;
	public static final int FACE_NINE = 8;
	public static final int FACE_EIGHT = 7;
	public static final int FACE_SEVEN = 6;
	public static final int FACE_SIX = 5;
	public static final int FACE_FIVE = 4;
	public static final int FACE_FOUR = 3;
	public static final int FACE_THREE = 2;
	public static final int FACE_TWO = 1;

	public static final int SUIT_SPADES = 4;
	public static final int SUIT_HEARTS = 3;
	public static final int SUIT_DIAMONDS = 2;
	public static final int SUIT_CLUBS = 1;

	public static final int[] FACES = new int[] { FACE_ACE, FACE_KING, FACE_QUEEN, FACE_JACK, FACE_TEN, FACE_NINE, FACE_EIGHT, FACE_SEVEN, FACE_SIX, FACE_FIVE, FACE_FOUR, FACE_THREE, FACE_TWO };
	public static final int[] SUITS = new int[] { SUIT_SPADES, SUIT_HEARTS, SUIT_DIAMONDS, SUIT_CLUBS };
	public static final int CARD_ACE = 1 << FACE_ACE;
	public static final int CARD_AKQJT = 0b11111000000000;
	public static final int CARD_5432A = 0b11111;

	static class CardMap {
		private double key;
		private int value;

		private CardMap(double key, int value) {
			this.key = key;
			this.value = value;
		}

		private double getKey() {
			return key;
		}

		private int getValue() {
			return value;
		}

	}

}