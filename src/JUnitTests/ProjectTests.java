package JUnitTests;

import junit.framework.TestCase;
import mvc.controller.Controller;
import mvc.model.card.Ariadni;
import mvc.model.card.Card;
import mvc.model.card.Minotavro;
import mvc.model.card.SimpleCard;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.pawn.Pawn;
import mvc.model.player.Player;

public class ProjectTests extends TestCase {


    public void testControllerInitialization(){
        Controller controller = new Controller();
        assertNotNull(controller);
    }

    public void testSimpleCard(){
        Controller controller = new Controller();
        Player player = controller.getPlayer1();
        Card simpleCard = null;
        int i = 0;
        for(; i < 8; i++){
            if(player.getHand().get(i) instanceof SimpleCard){
                simpleCard = (SimpleCard)player.getHand().get(i);
                break;
            }
        }
        assert simpleCard != null;
        player.createArch(simpleCard.getPath());
        player.playCard(i, player);
        Pawn pawn = player.getPawns().get(controller.getPathIndex(simpleCard.getPath(), null));
        assert pawn != null;
        assertEquals(1, pawn.getPositionNum());


        assertTrue(player.isPlayedCard());

        assertNull(player.getHand().get(i));
    }

    public void testStartingWithAriadniCard(){
        Controller controller = new Controller();
        Player player = controller.getPlayer1();
        Card card = null;

            for(int i = 0; i < 84; i++){
                card = controller.getCardDeck().pop();
                if(card instanceof Ariadni){
                    break;
                }
            }

        assert card != null;
            player.getHand().set(0, card);
        try{
            player.playCard(0, player);
            fail();
        } catch (IllegalMoveException e) {
            assertTrue(true);
        }

        assertTrue(!player.isPlayedCard());
    }

    public void testPlayingMinotavroWithoutPawnInPath(){
        Controller controller = new Controller();
        Player player = controller.getPlayer1();
        Card card = null;

        for(int i = 0; i < 84; i++){
            card = controller.getCardDeck().pop();
            if(card instanceof Minotavro){
                break;
            }
        }

        assert card != null;
        player.getHand().set(0, card);
        try{
            player.playCard(0, controller.getOppositePlayer(player));
            fail();
        } catch (IllegalMoveException e) {
            assertTrue(true);
        }

        assertFalse(player.isPlayedCard());
    }

    public void testEndConditionWhenCardDeckEmpty(){
        Controller controller = new Controller();
        while(!controller.getCardDeck().isEmpty()){
            controller.getCardDeck().pop();
        }
        assertTrue(controller.checkEndConditions());
    }

    public void testDiscardCard(){
        Controller controller = new Controller();
        Player player = controller.getPlayer1();
        player.discardCard(0, controller.getDiscardDeck());
        assertTrue(player.isPlayedCard());
        assertNull(player.getHand().get(0));
    }

    public void testTurns(){
        Controller controller = new Controller();
        Player player1 = controller.getPlayer1();
        Player player2 = controller.getPlayer2();

        // the controller constructor randomly chooses a player to start
        if(player1.isTurn() && player2.isTurn()) fail();
        if(!player1.isTurn() && !player2.isTurn()) fail();

        if(player1.isTurn() && !player2.isTurn()) {
            assertTrue(true);
            controller.changeTurn();
            if(!player1.isTurn() && player2.isTurn()){
                assertTrue(true);
            }else{
                fail();
            }
        }
        if(!player1.isTurn() && player2.isTurn()) {
            assertTrue(true);
            controller.changeTurn();
            if(player1.isTurn() && !player2.isTurn()){
                assertTrue(true);
            }else{
                fail();
            }
        }
    }

    public void testInitialization(){
        Controller controller = new Controller();
        Player player = controller.getPlayer1();

        assertEquals(8, player.getHand().size());

        assertEquals(4, controller.getRareFinds().size());

        assertEquals(10, controller.getSimpleFinds().size());

        assertEquals(6, controller.getMurals().size());
    }



}
