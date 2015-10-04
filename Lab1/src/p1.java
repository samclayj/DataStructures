/*
PROGRAM NAME: Program 0, Read Information from File
PROGRAMMER:   Samuel Jentsch
CLASS:        CSC 241.001, Fall 2013
INSTRUCTOR:   Dr. D. Dunn
DATE STARTED: September 8, 2013
DUE DATE:     September 9, 2013
REFERENCES:   Computer Science
        Data Abstraction & Problem Solving with Java
  Janet J. Prichard and Frank M. Carrano             
       Dr. Dunn: assignment information sheet for Lab 1

PROGRAM PURPOSE:
a. This program reads in bridge hands (cards) from a file. Hands consist of 13 cards. Cards are in the form RANK|SUIT (AS = Ace of Spades).
b. The program displays the hand in an organized output.
c. The program calculates the point value of the bridge hand based on the following rules:
 Ace = 4; King = 3; Queen = 2; Jack = 1;
 void (no cards present in a suit) = 3; Singleton (1 card in a suit) = 2; Doubleton (2 cards in a suit) = 1;
 Long suit (5 or more cards in a suit) = 1 point for each card over 5 in number;
d. The program displays the calculated point value.

VARIABLE DICTIONARY:
   	CARD_FILE_NAME - string, contains file name for the file containing the hands or cards.
   
   	deck - int[][] used to hold cards. 0 - 3 of first index is used for suits, 0 - 12 of second index is used for cards.
   	hands - ArrayList<String>, used to hold the hands read in line by line as strings from the file.
   	
   	SPADES - int, used for SPADES index of deck.
   	CLUBS - int, used for CLUBS index of deck. 
   	DIAMONDS - int, used for the DIAMONDS deck.
   	HEARTS - int, used for HEARTS index of deck.
   
   	TWO - int, used to store the index of '2' in the deck.
   	THREE - int, used to store the index of '3' in the deck.
   	FOUR - int, used to store the index of '4' in the deck.
	FIVE - int, used to store the index of '5' in the deck.
	SIX - int, used to store the index of '6' in the deck.
	SEVEN - int, used to store the index of '6' in the deck.;
	EIGHT - int, used to store the index of '6' in the deck.
	NINE - int, used to store the index of '6' in the deck.
	TEN - int, used to store the index of '6' in the deck.
	JACK - int, used to store the index of '6' in the deck.
	QUEEN - int, used to store the index of '6' in the deck.
	KING - int, used to store the index of '6' in the deck.
	ACE - int, used to store the index of '6' in the deck.

ADTs:
   none

FILES USED:
   p1.dat - a file containing cards in lines corresponding to hands.


Test Cases:

Test Case 1:
Sample Input: 
  AH TS QH 7D 5C JS 3H KS 9D QC 4H AD 9C
Sample Output:
  CLUBS: 5 9 Q
  DIAMONDS: 7 9 A
  HEARTS: 3 4 Q A
  SPADES: 10 J K

  POINTS = 16

Test Case 2:
Sample Input:
  7C AD 9C QC TH 2H 8H 3C QH 5H AC TD JD
Sample Output:
  CLUBS: 3 7 9 Q A
  DIAMONDS: T J A
  HEARTS: 2 5 8 T Q
  SPADES:

  POINTS = 16

Test Case 3:
Sample Input:
   7C AD AD QC TH 2H 8H 3C QH 5H AC TD 6C
Sample Output:
  Error detected: Duplicate cards.

Test Case 4:
Sample Input:
   AD 9C QC TH 2H 8H 3C QH 5H AC TD JD
Sample Output: 
   Error detected: Less than 13 cards.

--------------------------------------------------------------------
 */

import java.io.*;
import java.util.*;

public class p1 {

	//The file for the hands.
	static final String CARD_FILE_NAME = "src/p1.dat";

	//The array for holding cards. Size is 52 when initialized.
	static int[][] deck;
	
	//ArrayList that holds strings corresponding to hands of cards. 
	static ArrayList<String> hands;
	
	//Index values for suits.
	static final int SPADES = 0;
	static final int CLUBS = 1;
	static final int DIAMONDS = 2;
	static final int HEARTS = 3;

	//Index values for ranks.
	static final int TWO = 0;
	static final int THREE = 1;
	static final int FOUR = 2;
	static final int FIVE = 3;
	static final int SIX = 4;
	static final int SEVEN = 5;
	static final int EIGHT = 6;
	static final int NINE = 7;
	static final int TEN = 8;
	static final int JACK = 9;
	static final int QUEEN = 10;
	static final int KING = 11;
	static final int ACE = 12;

	public static void main(String[] args) {
		readHands();
		
		for(int i = 0; i < hands.size(); i++) {
			refreshDeck();
			
			//checkHand returns -1 if the method failed, 0 if the method passed.
			int handPassed = checkHand(hands.get(i), i);
			
			if(handPassed == -1) {
				continue;
			}
			
			displayHand(hands.get(i));
			
			System.out.printf("\n\n\t\tPOINTS = %d\n", evaluateHand());
			System.out.printf("-----------------------------------------------------------------------------------------------------------\n");

		}
	}//end main 

	public static void refreshDeck() {
		//-----------------------------------------------------------------------------------------------------
		//Refreshes the deck array by initializing it and filling it with 0s. 0 corresponds to an empty space.
		//Precondition: a class variable named deck of type int[][].
		//Postcondition: deck is initialized as int[4][13] and filled with 0s.
		//-----------------------------------------------------------------------------------------------------
		deck = new int[4][13];
		for(int i = 0; i < deck.length; i++) {
			for(int j = 0; j < deck[i].length; j++)
			//0 stands for no card
			deck[i][j] = 0;
		}//end for
	}
	
	public static ArrayList<String> readHands() {
		//---------------------------------------------------------------------------------------------------------------------------------------
		//Reads "hands" of cards in as Strings line by line and store them in an ArrayList.
		//Precondition: a file existing at the location CARD_FILE_NAME containing hands of cards. One line corresponds to a single hand of cards.
		//Postcondition: each line of the file is read and stored as a string in the class variable ArrayList hands. 
		//--------------------------------------------------------------------------------------------------------------------------------------- 
		hands = new ArrayList<String>();

		try {
			File cardFile = new File(CARD_FILE_NAME);
			Scanner cardFileReader = new Scanner(cardFile);

			while(cardFileReader.hasNextLine()) {
				hands.add(cardFileReader.nextLine());
			}//end while
		}
		catch (FileNotFoundException ex) {
			System.out.print("\nError: File not found.");
		}
		catch(Exception ex) {
			System.out.print("\nError: Something really bad happened.");
		}

		return hands;
	}//end void readHands

	public static int checkHand(String hand, int line) {
		//------------------------------------------------------------------------------------------------------------------------------------------- 
		//Checks the string hand for input errors. 
		//For a hand to be correct it must conform to the following:
		// -hand.length() == 13
		// -hand must contain no duplicate cards.
		// -hand must contain cards of valid suits and ranks. (Valid Ranks: 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A) (Valid Suits: C, S, H, D)
		//Preconditions: the variable hand must be an initialized string containing cards (string containing rank and suit) separated by spaces.
		//				 The class variable deck must be available and initialized for use in the method.
		//Postcondition: The cards (substrings of hand) are checked to verify they meet the criteria discussed above. If they fail, the
		//    			 error -1 is returned. If hand passes the criteria 0 is returned. The class variable deck is populated with 1s where cards
		//				 where found, corresponding to their index discovered using the findCardIndex(String index) method.
		//--------------------------------------------------------------------------------------------------------------------------------------------

		int returnValue = 0;

		String[] cards = hand.split(" ");
		
		//Check if there are less than 13 cards.
		if(cards.length < 13) {
			System.out.printf("\nError: Hand on input line %d contains %d cards. Hands must contain 13 cards.\n", line, cards.length);
			returnValue = -1; 
		} 
		//Check if there are more than 13 cards.
		else if(cards.length > 13) {
			System.out.printf("\nError: Hand on input line %d contains %d cards. Hands must contain 13 cards.\n", line, cards.length);
			returnValue = -1;
		}
		else {
			for(int i = 0; i < cards.length; i++) {
				String card = cards[i];
				
				if(card.length() != 2) {
					//Check if the card has two characters.
					System.out.printf("\nError: Hand on input line %d. Card found missing suit or rank (only 1 character).\n", line);
					returnValue = -1;
					break;
				}
				
				int[] cardIndex = findCardIndex(card);
				if(cardIndex[0] < 0 || cardIndex[1] < 0) {
					//Find the index of the card in the deck. findCardIndex returns a negative value for an invalid card.
					System.out.printf("\nError: Hand on input line %d. Invalid rank/suit.\n", line);
					returnValue = -1;
					break;
				}
				
				if(deck[cardIndex[0]][cardIndex[1]] == 0) {
					//Check for duplicates.
					deck[cardIndex[0]][cardIndex[1]] = 1;
				} 
				else {
					System.out.printf("\nError: Hand on input line %d. Duplicate card.\n", line);
					returnValue = -1;
					break;
				}
				
			}//end for
		}//end else          

	return returnValue;
}//end checkHand

public static int[] findCardIndex(String card) {
	//--------------------------------------------------------------------------------------------------------------------------------------------
	//Find the suit index and rank index corresponding to the two dimensional deck array using two switch statements: one for rank, one for suit.
	//Precondition: a string in the form (RANK)(SUIT) passed as a parameter. Constants defined corresponding to suit and rank indices.
	//Postcondition: the method creates an int[] called arrayIndex with arrayIndex[0] corresponding to the index of the suit, and arrayIndex[1]
    //				 corresponding to the index of the rank. arrayIndex is returned. Note that a negative rankIndex and/or suitIndex is returned
	//				 if an invalid rank/suit is processed.
	//--------------------------------------------------------------------------------------------------------------------------------------------
	int rankIndex = -1;
	char rank = card.charAt(0);

	int rankValue = 0;
	switch(rank) { //Find the rank index based on the character value for rank.
	case '2':
		rankValue = TWO;
		break;
	case '3':
		rankValue = THREE;
		break;
	case '4':
		rankValue = FOUR;
		break;
	case '5':
		rankValue = FIVE;
		break;
	case '6':
		rankValue = SIX;
		break;
	case '7':
		rankValue = SEVEN;
		break;
	case '8':
		rankValue = EIGHT;
		break;
	case '9':
		rankValue = NINE;
		break;
	case 'T':
		rankValue = TEN;
		break;
	case 'J':
		rankValue = JACK;
		break;
	case 'Q':
		rankValue = QUEEN;
		break;
	case 'K':
		rankValue = KING;
		break;
	case 'A':
		rankValue = ACE;
		break;
	default:
		//System.out.printf("\nError: Invalid rank %c,", rank);
		rankValue = -1;
	}//end switch

	if(rankValue != -1)
		rankIndex = rankValue;
	
	int suitIndex = -1;
	int suitValue = 0;
	char suit = card.charAt(1);
	switch(suit) { //Find the suit index based on the character value for suit.
	case 'S':
		suitValue = SPADES;
		break;
	case 'C':
		suitValue = CLUBS;
		break;
	case 'D':
		suitValue = DIAMONDS;
		break;
	case 'H':
		suitValue = HEARTS;
		break;
	default:
		//System.out.printf("\nError: Invalid suit %c.", suit);
		suitValue = -1;
	}

	if(suitValue != -1)
		suitIndex = suitValue;

	int[] arrayIndex = {suitIndex, rankIndex};
	return arrayIndex;
}

public static void displayHand(String hand) {
	//----------------------------------------------------------------------------------------------------------------------------------------------------
	//This method displays the hand using the deck class variable. Only cards present in the hand are displayed. If a card is present in the deck,
	//a 1 will be present at the deck index. If there is a 1, findCardString(int index) is used to take the card index and return a String for the card
	//corresponding to that specific index.
	//Precondition- Class variable deck initialized and populated with 0s (no card) or 1s (card). Constants corresponding to suit and rank indices.
	//Postcondition- Each suit is printed followed by the cards present in the current hand that are a part of that suit.
	//----------------------------------------------------------------------------------------------------------------------------------------------------

	System.out.printf("-----------------------------------------------------------------------------------------------------------\n");
	
	for(int i = 0; i < deck.length; i++) {
		
		//Switch suit based current deck index
		String suit = "";
		switch(i) {
		case SPADES:
			suit = "SPADES";
			break;
		case CLUBS:
			suit = "CLUBS";
			break;
		case DIAMONDS:
			suit = "DIAMONDS";
			break;
		case HEARTS:
			suit = "HEARTS";
			break;
		}//end switch
		
		System.out.format("\n%10s      ", suit);
		
		for(int j = 0; j < deck[i].length; j++) {
			if(deck[i][j] == 1) {
				String cardString = findCardString(j);
				System.out.printf("%s     ", cardString);
			}//end if
		}//end inner for
	}//end outer for
	
}//end displayHand

public static String findCardString(int index) {
	//------------------------------------------------------------------------------------------------------------
	//This method takes a rank index and finds the String rank using a switch statement utilizing the predefined
	//rank index constants.
	//Precondition: Predefined constants for rank indices. 
	//Post condition: This method returns a String corresponding to the card value based on the card's rank index.
	//------------------------------------------------------------------------------------------------------------
	String cardString = "";
	
	switch(index) {
	case TWO:
		cardString += "2";
		break;
	case THREE:
		cardString += "3";
		break;
	case FOUR:
		cardString += "4";
		break;
	case FIVE:
		cardString += "5";
		break;
	case SIX:
		cardString += "6";
		break;
	case SEVEN:
		cardString += "7";
		break;
	case EIGHT:
		cardString += "8";
		break;
	case NINE:
		cardString += "9";
		break;
	case TEN:
		cardString += "T";
		break;
	case JACK:
		cardString += "J";
		break;
	case QUEEN:
		cardString += "Q";
		break;
	case KING:
		cardString += "K";
		break;
	case ACE:
		cardString += "A";
		break;
	}//end switch
	
	return cardString;
}

public static int evaluateHand() {
	//----------------------------------------------------------------------------------------------------------
	//This method calculates the value of the hand. It uses switch statements utilizing predefined constants
	//for rank indices and suit indices. It keeps track of the number of cards in each suit to calculate the 
	//point value of Void, Singleton, Doubleton, and Long Suits. A switch statement is used to add point values
	//for card indices corresponding to cards with point values (JACK, QUEEN, KING, ACE).
	//Precondition: Class deck variable initialized with card values (0s and 1s), predefined constants for rank 
	//				and suit indices.
	//Postcondition: Returns the points value calculated.
	//----------------------------------------------------------------------------------------------------------
	
	int[] cardsInSuits = new int[4];
	
	int points = 0;
	for(int i = 0; i < deck.length; i++) {
		for (int j = 0; j < deck[i].length; j++) {

			if(deck[i][j] == 1) {
				//There is a card, evaluate the points.
				switch(i) { //Find number of cards in each suit
				case SPADES:
					cardsInSuits[SPADES]++;
					break;
				case CLUBS:
					cardsInSuits[CLUBS]++;
					break;
				case DIAMONDS:
					cardsInSuits[DIAMONDS]++;
					break;
				case HEARTS:
					cardsInSuits[HEARTS]++;
					break;
				default:
					System.out.print("\nERROR: Evaluate Hand");
				}//end switch
				
				switch(j) { //Points for card values
				case JACK:
					points += 1;
					break;
				case QUEEN:
					points += 2;
					break;
				case KING:
					points += 3;
					break;
				case ACE:
					points += 4;
					break;
				}//end switch
			}//end if
		}//end inner for
	}//end outer for
	
	for(int i = 0; i < cardsInSuits.length; i++) {
		if(cardsInSuits[i] == 0) //Void
			points += 3;
		if(cardsInSuits[i] == 1) //Singleton
			points += 2;
		if(cardsInSuits[i] == 2) //Doubleton
			points += 1;
		if(cardsInSuits[i] > 5) //Long suit
			points += cardsInSuits[i] - 5;
	}//end for
	
	return points;
}//end evaluateHand 

}//end p1

