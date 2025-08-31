package mvc.model.card;

import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Board;
import mvc.model.path.Path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * CardDeck class ia a stack implementation using Array.
 * The capacity of the array is set to 100 (100 cards).
 */
public class CardDeck implements CardStack, Serializable {
    private final static int capacity = 100;
    private Card[] cards;
    private int top;
    private List<SimpleCard> simpleCards;
    private List<Ariadni> ariadniCards;
    private List<Minotavro> minotavroCards;

    /**
     * CardDeck constructor, to create the card deck. and then calls the initializeDeck to initialize the deck with 100 cards
     */
    public CardDeck(Board board) {
        this.cards = new Card[capacity];
        this.top = -1;
        assert cards != null; //postcondition
        this.simpleCards = new ArrayList<>();
        createSimpleCards(board);
        this.ariadniCards = new ArrayList<>();
        createAriadniCards(board);
        this.minotavroCards = new ArrayList<>();
        createMinotavroCards(board);
        initializeDeck();
    }

    /**
     * method push : to put a card in the stack
     * @param card that is pushed to the stack
     * @post added a card to the deck
     * @throws IllegalMoveException if we try to add to the card deck
     */
    @Override
    public void push(Card card) throws IllegalMoveException {
        throw new IllegalMoveException("Cant add to a Card Deck");
    }

    /**
     * method createSimpleCards create 20 cards of each path (each card two times)
     * so in total 80 cards
     * @post having 80 simple cards initialized
     */
    public void createSimpleCards(Board board) {
        Path[] paths = new Path[4];
        paths[0] = board.getKnossos();
        paths[1] = board.getMalia();
        paths[2] = board.getPhaistos();
        paths[3] = board.getZakros();
        for(Path path : paths) {
            int value = 1;
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 2; j++){
                    String imagePath = "project_assets/images/cards/" + path.getName().toLowerCase() + value + ".jpg";
                    SimpleCard simpleCard = new SimpleCard(value, path, imagePath);
                    simpleCards.add(simpleCard);
                }
                value++;
            }
        }
    }

    /**
     * method createAriadniCards create 3 cards for each path
     * so in total 12 cards
     * @post having 12 ariadni cards initialized
     */
    public void createAriadniCards(Board board) {
        Path[] paths = new Path[4];
        paths[0] = board.getKnossos();
        paths[1] = board.getMalia();
        paths[2] = board.getPhaistos();
        paths[3] = board.getZakros();
        for(Path path : paths) {
            for(int i = 0; i < 3; i++){
                String imagePath = "project_assets/images/cards/" + path.getName().toLowerCase() + "Ari.jpg";
                Ariadni ariadni = new Ariadni(path, imagePath);
                ariadniCards.add(ariadni);
            }
        }
    }

    /**
     * method createMinotavroCards create 2 cards for each path
     * so in total 8 cards
     * @post having 8 Minotavro cards initialized
     */
    public void createMinotavroCards(Board board) {
        Path[] paths = new Path[4];
        paths[0] = board.getKnossos();
        paths[1] = board.getMalia();
        paths[2] = board.getPhaistos();
        paths[3] = board.getZakros();
        for(Path path : paths) {
            for(int i = 0; i < 2; i++){
                String imagePath = "project_assets/images/cards/" + path.getName().toLowerCase() + "Min.jpg";
                Minotavro minotavro = new Minotavro(path, imagePath);
                minotavroCards.add(minotavro);
            }
        }
    }

    /**
     * method initializeDeck : initializes the deck with 100 cards. 80 SimpleCards, 12 AriadneCards, and 8 MinotaurCards.
     * it shuffles the cards,
     * The cards are added in a random order.
     * @pre the stack is empty there in no cards
     * @post a deck that is shuffled that has 100 cards.
     */

    public void initializeDeck() {
        List<Card> deck = new ArrayList<>();
        deck.addAll(simpleCards);
        deck.addAll(ariadniCards);
        deck.addAll(minotavroCards);

        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
        for(int i = 0; i < deck.size(); i++){
            cards[i] = deck.get(i);
        }
        top = deck.size() - 1;
    }

    /**
     * method pop : to pop a card from the stack when the player pick a card
     *
     * @return the top card in the stack
     * @throws IllegalStateException if its empty then end game
     * @pre the player hasn't picked a card yet
     * @post the player picked a card and  removed it from the stack
     */
    public Card pop() throws IllegalStateException {
        if(isEmpty()) throw new IllegalStateException("Deck is empty");
        Card card = cards[top];
        cards[top] = null;
        top--;
        return card;
    }

    /**
     * method isEmpty : to observe if the stack is empty (to end the game)
     *
     * @return true if stack is empty, false otherwise
     * @pre we dont know if the stack is empty
     * @post we know if the stack is empty or not
     */
    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * method clear : to clear the stack
     * @post the stack is empty
     */
    @Override
    public void clear() {
        for(int i = 0; i < cards.length; i++) {
            cards[i] = null;
        }
        top = -1;
    }

    /**
     * method getsize : to return the number of the remaining cards in the deck
     * @return the size of the stack
     */
    @Override
    public int getSize() {
        return top+1;
    }

}