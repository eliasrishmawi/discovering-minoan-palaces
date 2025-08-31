package mvc.model.card;


/**
 * This interface represents the stack of 100 cards
 */

public interface CardStack{

    /**
     * method push : to put a card in the stack
     * @param card that will be pushed to the stack
     * @post added a card to the deck
     */
    void push(Card card);



    /**
     * method isEmpty : to observe if the stack is empty (to end the game)
     * @return true if stack is empty, false otherwise
     * @pre we dont know if the stack is empty
     * @post we know if the stack is empty or not
     */
    boolean isEmpty ();

    /**
     * method clear : to clear the stack
     * @post the stack is empty
     */
    void clear ();

    /**
     * method getsize : to return the number of the remaining cards in the deck
     * @return the size of the stack
     */
    int getSize();

}
