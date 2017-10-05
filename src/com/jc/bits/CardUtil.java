package com.jc.bits;

import java.util.HashMap;
import java.util.Map;

public class CardUtil {
	public static final Map<Character, Integer> faceMap = new HashMap<>();
	public static final Map<Character, Integer> suitMap = new HashMap<>();
	static {
		faceMap.put('2', Cards.FACE_TWO);
		faceMap.put('3', Cards.FACE_THREE);
		faceMap.put('4', Cards.FACE_FOUR);
		faceMap.put('5', Cards.FACE_FIVE);
		faceMap.put('6', Cards.FACE_SIX);
		faceMap.put('7', Cards.FACE_SEVEN);
		faceMap.put('8', Cards.FACE_EIGHT);
		faceMap.put('9', Cards.FACE_NINE);
		faceMap.put('T', Cards.FACE_TEN);
		faceMap.put('J', Cards.FACE_JACK);
		faceMap.put('Q', Cards.FACE_QUEEN);
		faceMap.put('K', Cards.FACE_KING);
		faceMap.put('A', Cards.FACE_ACE);

		suitMap.put('C', Cards.SUIT_CLUBS);
		suitMap.put('D', Cards.SUIT_DIAMONDS);
		suitMap.put('H', Cards.SUIT_HEARTS);
		suitMap.put('S', Cards.SUIT_SPADES);
	}

	public static int makeCard(char face, char suit) {
		return Cards.makeCard(faceMap.get(face), suitMap.get(suit));
	}

	public static int[] makeCards(String card) {
		String[] cards = card.split(" ");
		int[] result = new int[cards.length];
		for (int i = 0; i < cards.length; i++) {
			String faceCard = cards[i];
			result[i] = makeCard(faceCard.charAt(0), faceCard.charAt(1));
		}
		return result;
	}
}
