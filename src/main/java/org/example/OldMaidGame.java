package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class OldMaidGame {
    public static void main(String[] args) {
        int numPlayers = 4;
        List<Player> players = new ArrayList<>();

        // Create players
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i));
        }

        // Set next player references
        for (int i = 0; i < numPlayers; i++) {
            players.get(i).setNextPlayer(players.get((i + 1) % numPlayers));
        }

        // Create and shuffle the deck of cards
        List<Card> deck = new ArrayList<>();
        // Create and populate the deck with cards

        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(suit, value));
            }
        }

// Add the Joker card
        deck.add(new Card("Joker", "Joker"));

        Collections.shuffle(deck);

        // Deal cards to players
        for (int i = 0; i < deck.size(); i++) {
            players.get(i % numPlayers).addToHand(deck.get(i));
        }

        // Create and start player threads
        List<Thread> playerThreads = new ArrayList<>();
        for (Player player : players) {
            playerThreads.add(new Thread(player));
        }

        for (Thread thread : playerThreads) {
            thread.start();
        }

        // Wait for all player threads to finish
        for (Thread thread : playerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Find and print the loser
        for (Player player : players) {
            if (!player.hasMatchingPair()) {
                System.out.println(player.getName() + " is the loser.");
            }
        }
    }
}
