package org.example;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Player implements Runnable {
    private String name;
    private List<Card> hand;
    private Player nextPlayer;
    private Lock lock;
    public String getName() {
        return name;
    }
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public boolean hasMatchingPair() {
        for (Card card : hand) {
            for (Card otherCard : hand) {
                if (card != otherCard && card.getSuit().equals(otherCard.getSuit()) &&
                        card.getValue().equals(otherCard.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeMatchingPair() {
        Iterator<Card> iterator = hand.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            for (Card otherCard : hand) {
                if (card != otherCard && card.getSuit().equals(otherCard.getSuit()) &&
                        card.getValue().equals(otherCard.getValue())) {
                    iterator.remove();
                    hand.remove(otherCard);
                    break;
                }
            }
        }
    }

    public void run() {
        while (!hand.isEmpty()) {
            lock.lock();
            try {
                if (!hasMatchingPair()) {
                    // Draw a random card from the next player
                    int index = new Random().nextInt(nextPlayer.hand.size());
                    Card drawnCard = nextPlayer.hand.remove(index);
                    addToHand(drawnCard);

                    System.out.println(name + " drew " + drawnCard.getValue() + " of " + drawnCard.getSuit());

                    if (hasMatchingPair()) {
                        removeMatchingPair();
                        System.out.println(name + " discarded a matching pair.");
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        System.out.println(name + " has no more cards. Game over for " + name);
    }
}
