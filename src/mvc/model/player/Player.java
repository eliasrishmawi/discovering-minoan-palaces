package mvc.model.player;

import mvc.model.card.*;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.findings.Findings;
import mvc.model.findings.Murals;
import mvc.model.findings.RareFinds;
import mvc.model.findings.SimpleFinds;
import mvc.model.path.Path;
import mvc.model.pawn.Archaeologist;
import mvc.model.pawn.Pawn;
import mvc.model.pawn.Theseus;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this class represents the Player.
 */
public class Player implements Serializable {
    private final PlayerColor color;
    private final String name;
    //private int score;
    private boolean turn;
    private boolean playedCard;
    private List<Card> hand;
    private Card lastCardKnossos;
    private int valueOfLastSimpleCardKnossos;
    private Card lastCardMalia;
    private int valueOfLastSimpleCardMalia;
    private Card lastCardPhaistos;
    private int valueOfLastSimpleCardPhaistos;
    private Card lastCardZakros;
    private int valueOfLastSimpleCardZakros;
    private List<RareFinds> rareFinds;
    private List<Murals> murals;
    private int statuettesCount;
    private List<Pawn> pawns;
    private final String soundPath;

    private int availableArchaeologists;
    private boolean theseusAvailable;


    /**
     * Constructor for the Player
     * draws 8 cards from the CardDeck and initializes other fields.
     * @param name of the player
     * @param color of the player
     * @param cardDeck The deck from which to draw cards for the player's hand.
     * @param soundPath of the player
     * @post The player starts with 8 cards in hand, no findings, and 0 score.
     */
    public Player(String name, PlayerColor color, CardDeck cardDeck, String soundPath) {
        this.name = name;
        this.color = color;
        //this.score = 0;
        this.turn = false;
        this.playedCard = false;
        this.hand = new ArrayList<>();
        this.lastCardKnossos = null;
        this.lastCardMalia = null;
        this.lastCardPhaistos = null;
        this.lastCardZakros = null;
        valueOfLastSimpleCardKnossos = 0;
        valueOfLastSimpleCardMalia = 0;
        valueOfLastSimpleCardPhaistos = 0;
        valueOfLastSimpleCardZakros = 0;
        this.rareFinds = new ArrayList<>();
        this.murals = new ArrayList<>();
        this.statuettesCount = 0;
        this.pawns = new ArrayList<>();
        this.soundPath = soundPath;
        availableArchaeologists = 3;
        theseusAvailable = true;

        for(int i = 0; i < 8; i++){
            this.hand.add(cardDeck.pop());
        }

        for(int i = 0; i < 4; i++){
            pawns.add(null);
        }

        for(int i = 0; i < 4; i++){
            rareFinds.add(null);
        }

        assert hand.size() == 8 : "the player will have 8 cards at the start";

    }

    /**
     * Accessor to the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Accessor to the color of the player
     *
     * @return the color of the player
     */
    public Color getColor() {
        return this.color.getAwtColor();
    }

    /**
     * calculates the score of the player
     * @return the score of the player
     */
    public int getScore() {
        int muralsScore = 0;
        for(Murals mural : murals){
            if(mural != null){
                muralsScore += mural.getPoints();
            }
        }
        int rareFindsScore = 0;
        for(RareFinds rareFind : rareFinds){
            if(rareFind != null){
                rareFindsScore += rareFind.getPoints();
            }
        }
        int statuettesScore = 0;
        if(statuettesCount == 0){
            statuettesScore = 0;
        }else if(statuettesCount == 1){
            statuettesScore = -20;
        }else if(statuettesCount == 2){
            statuettesScore = -15;
        }else if(statuettesCount == 3){
            statuettesScore = 10;
        }else if(statuettesCount == 4){
            statuettesScore = 15;
        }else if(statuettesCount == 5){
            statuettesScore = 30;
        }else if(statuettesCount >= 6){
            statuettesScore = 50;
        }
        int positionPoints = 0;
        for(Pawn pawn : pawns){
            if(pawn != null){
                if(pawn instanceof Archaeologist){
                    positionPoints += pawn.getPosition().getPoints();
                }else if(pawn instanceof Theseus){
                    positionPoints += 2 * pawn.getPosition().getPoints();
                }
            }
        }
        int playerScore = muralsScore + rareFindsScore + statuettesScore + positionPoints;
        return playerScore;
    }


    /**
     * to know if it is the player's turn
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isTurn() {
        return this.turn;
    }

    /**
     * to update the player's turn
     * @param turn of the player
     */
    public void setTurn(boolean turn) {
        this.turn = turn;
        if(turn == true) setPlayedCard(false);
    }

    /**
     * to access the players cards
     * @return the hand of the player
     */
    public List<Card> getHand() {
        return this.hand;
    }

    /**
     * Draws a card from the deck and adds it to the player's hand. Then make its turn false
     * @param cardDeck the deck from which to draw the card.
     * @return the index of the card in the hand
     */
    public int drawCard(CardDeck cardDeck) throws IllegalMoveException {
        //if(!isTurn()) throw new IllegalMoveException("Cant draw card it is not your turn");
        if(!isPlayedCard()) throw new IllegalMoveException("cant draw a card. You need to play a card or discard it first");
        try{
            int i = 0;
            for(; i < 8; i++){
                if(hand.get(i) == null) break;
            }
            this.hand.set(i, cardDeck.pop());
            return i;
        } catch (IllegalStateException e) {
            throw new IllegalMoveException("the cardDeck is empty. The game should end");
        }
    }

    /**
     * Throw a card from the hand and adds it to the discard deck.
     * @param discardDeck the deck to which we throw a card.
     * @param cardIndex that we want to throw
     */
    public void discardCard(int cardIndex, DiscardDeck discardDeck) {
        discardDeck.push(hand.get(cardIndex));

        this.hand.set(cardIndex, null);
        setPlayedCard(true);
    }

    /**
     * Plays a card from the player's hand.
     * @param cardIndex The index of the card in the hand to play.
     * @param player that its pawns are effected by the card thrown
     */
    public void playCard(int cardIndex, Player player) throws IllegalMoveException{
        Card card = this.hand.get(cardIndex);
        if(card == null) throw new IllegalMoveException("there is no card there");
        Card prvCard = null;
        Path path = card.getPath();
        if(Objects.equals(path.getName(), "Knossos")){
            prvCard = lastCardKnossos;
        }else if(Objects.equals(path.getName(), "Malia")){
            prvCard = lastCardMalia;
        }else if(Objects.equals(path.getName(), "Phaistos")){
            prvCard = lastCardPhaistos;
        }else if(Objects.equals(path.getName(), "Zakros")){
            prvCard = lastCardZakros;
        }
        if(prvCard == null){
            if(card instanceof Ariadni) throw new IllegalMoveException("Cant start a path with an ariadni card");
        }else{
            if(card instanceof SimpleCard){
                if(!((SimpleCard) card).matchCard(prvCard)){
                    throw new IllegalMoveException("Cant play the Card: The card that you are trying to play has smaller value than the previous one");
                }
            }
        }
        int i = -1;
        for(Pawn pawn : player.getPawns()){
            i++;
            if(pawn == null) continue;
            if(pawn.getPath() == path){
                break;
            }
        }
        Pawn pawn = player.getPawns().get(i);
        if(prvCard != null || this != player){
            card.play(pawn);
        }

        if(card instanceof SimpleCard){
            if(Objects.equals(path.getName(), "Knossos")){
                lastCardKnossos = card;
            }else if(Objects.equals(path.getName(), "Malia")){
                lastCardMalia = card;
            }else if(Objects.equals(path.getName(), "Phaistos")){
                lastCardPhaistos = card;
            }else if(Objects.equals(path.getName(), "Zakros")){
                lastCardZakros = card;
            }
        }

        hand.set(cardIndex, null);
        setPlayedCard(true);
    }

    /**
     * Accessor for the player's rare finds
     * @return the rare finds of the player
     */
    public List<RareFinds> getRareFinds() {
        return this.rareFinds;
    }

    /**
     * Accessor of the murals that the player have
     * @return the player's murals
     */
    public List<Murals> getMurals() {
        return this.murals;
    }

    /**
     * Captures a finding and adds it to the appropriate list.
     * @param finding The finding to capture.
     * @post added the finding to the player's collection
     */
    public void captureFind(Findings finding) {
        if (finding instanceof Murals) {
            murals.add((Murals) finding);
        } else if(finding instanceof RareFinds) {
            if(((RareFinds) finding).getPath().getName().equals("Knossos")){
                rareFinds.set(0, (RareFinds) finding);
            }else if(((RareFinds) finding).getPath().getName().equals("Malia")){
                rareFinds.set(1, (RareFinds) finding);
            }else if(((RareFinds) finding).getPath().getName().equals("Phaistos")){
                rareFinds.set(2, (RareFinds) finding);
            }else if(((RareFinds) finding).getPath().getName().equals("Zakros")){
                rareFinds.set(3, (RareFinds) finding);
            }
        }
        else if(finding instanceof SimpleFinds) {
            captureStatuette();
        }
    }

    /**
     * Accessor of how many statuettes does the player have
     * @return the player's statuettes count
     */
    public int getStatuettesCount() {
        return this.statuettesCount;
    }

    /**
     * Increments the count of captured statuettes.
     */
    public void captureStatuette() {
        this.statuettesCount++;
    }


    /**
     * create an Archaeologist and add it to the array list of the player
     * using the variable availableArchaeologists,
     * if (availableArchaeologists <= 0) then cant assign any more Archs and throw exception
     * otherwise availableArchaeologists--
     * @param path of the Archaeologist
     */
    public void createArch(Path path) throws IllegalMoveException {
        if(availableArchaeologists <= 0) throw new IllegalMoveException("Cant create any more archaeologists. You already created 3");
        Pawn pawn = new Archaeologist(path, this);
        if(Objects.equals(path.getName(), "Knossos")){
            pawns.set(0, pawn);
        }else if(Objects.equals(path.getName(), "Malia")){
            pawns.set(1, pawn);
        }else if(Objects.equals(path.getName(), "Phaistos")){
            pawns.set(2, pawn);
        }else{
            pawns.set(3, pawn);
        }
        availableArchaeologists--;
    }

    /**
     * create a Theseus and add it to the array list of the player
     * using the variable theseusAvailable,
     * if (!theseusAvailable) then cant assign anymore Theseus and throw exception
     * otherwise theseusAvailable = false
     * @param path of the Theseus
     */
    public void createTheseus(Path path) throws IllegalMoveException {
        if(!isTheseusAvailable()) throw new IllegalMoveException("Cant create any more Theseus. You already created one");
        Pawn pawn = new Theseus(path, this);
        if(Objects.equals(path.getName(), "Knossos")){
            pawns.set(0, pawn);
        }else if(Objects.equals(path.getName(), "Malia")){
            pawns.set(1, pawn);
        }else if(Objects.equals(path.getName(), "Phaistos")){
            pawns.set(2, pawn);
        }else{
            pawns.set(3, pawn);
        }
        theseusAvailable = false;
    }

    /**
     * Check the number of remaining Archaeologists.
     * @return the number of unassigned Archaeologists.
     */
    public int getAvailableArchaeologists() {
        return availableArchaeologists;
    }

    /**
     * Check if Theseus is still available.
     * @return true if Theseus is unassigned, false otherwise.
     */
    public boolean isTheseusAvailable() {
        return theseusAvailable;
    }

    /**
     * method to reset the player
     * @post the player is initialed with its default variables
     */
    public void resetPlayer(){

    }

    /**
     * Accessor to the Archaeologists in the pawn list
     * using if instance of Archaeologist ...
     * @return all the archaeologists in the pawn list
     */
    public List<Archaeologist> getArchaeologists(){
        List<Archaeologist> archaeologists = new ArrayList<>();
        for(Pawn pawn : pawns){
            if(pawn instanceof Archaeologist){
                archaeologists.add((Archaeologist) pawn);
            }
        }
        return archaeologists;
    }

    /**
     * Accessor to the Theseus in the pawn list
     * using if instance of Theseus ...
     * @return the Theseus in the pawn list
     */
    public Theseus getTheseus(){
        for(Pawn pawn : pawns){
            if(pawn instanceof Theseus){
                return (Theseus) pawn;
            }
        }
        return null;
    }

    /**
     * method to access all the pawns in the pawns list
     * @return all the pawns of the player
     */
    public List<Pawn> getPawns(){
        return pawns;
    }

    /**
     * Accessor of the last played card in knossos
     * @return the last played card in knossos
     */
    public Card getLastCardKnossos() {
        return lastCardKnossos;
    }

    /**
     * Transformer to the last played card in knossos
     * @param lastCardKnossos that the player played
     */
    public void setLastCardKnossos(Card lastCardKnossos) {
        this.lastCardKnossos = lastCardKnossos;
    }

    /**
     * Accessor of the last played card in malia
     * @return the last played card in malia
     */
    public Card getLastCardMalia() {
        return lastCardMalia;
    }

    /**
     * Transformer to the last played card in malia
     * @param lastCardMalia that the player played
     */
    public void setLastCardMalia(Card lastCardMalia) {
        this.lastCardMalia = lastCardMalia;
    }

    /**
     * Accessor of the last played card in phaistos
     * @return the last played card in phaistos
     */
    public Card getLastCardPhaistos() {
        return lastCardPhaistos;
    }

    /**
     * Transformer to the last played card in phaistos
     * @param lastCardPhaistos that the player played
     */
    public void setLastCardPhaistos(Card lastCardPhaistos) {
        this.lastCardPhaistos = lastCardPhaistos;
    }

    /**
     * Accessor of the last played card in zakros
     * @return the last played card in zakros
     */
    public Card getLastCardZakros() {
        return lastCardZakros;
    }

    /**
     * Transformer to the last played card in zakros
     * @param lastCardZakros that the player played
     */
    public void setLastCardZakros(Card lastCardZakros) {
        this.lastCardZakros = lastCardZakros;
    }

    /**
     * method to see if the player has played a card
     * @return true if the player has played a card, false otherwise
     */
    public boolean isPlayedCard() {
        return playedCard;
    }

    /**
     * method to set the played card
     */
    public void setPlayedCard(boolean playedCard) {
        this.playedCard = playedCard;
    }

    /**
     * getter of the sound path
     * @return the soundpath of the player
     */
    public String getSoundPath(){
        return soundPath;
    }
}
