package mvc.model.card;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents the discard deck
 */
public class DiscardDeck implements CardStack{
    private List<Card> cards;
    private int top;
    public DiscardDeck() {
        cards = new ArrayList<Card>();
        top = -1;
    }
    /**
     * method push : to put a card in the stack
     * @param card that will be pushed in the discard deck
     * @post added a card to the deck
     */
    @Override
    public void push(Card card) {
        cards.add(card);
        top++;
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
     *
     * @post the stack is empty
     */
    @Override
    public void clear() {
        cards.clear();
    }

    /**
     * method getsize : to return the number of the remaining cards in the deck
     *
     * @return the size of the stack
     */
    @Override
    public int getSize() {
        return top+1;
    }
}
