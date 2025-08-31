package mvc.controller;

import mvc.model.card.*;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.findings.Findings;
import mvc.model.findings.Murals;
import mvc.model.findings.RareFinds;
import mvc.model.findings.SimpleFinds;
import mvc.model.path.Board;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.pawn.Archaeologist;
import mvc.model.pawn.Pawn;
import mvc.model.pawn.Theseus;
import mvc.model.player.Player;
import mvc.model.player.PlayersMusic;
import mvc.view.GraphicUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static mvc.model.player.PlayerColor.*;

/**
 * Controller is the master of the game and controls all the operations executed
 * @author Elias Rishmawi
 */

public class Controller implements Serializable {
    private Player player1;
    private Player player2;
    private PlayersMusic music;
    private Board board;
    private CardDeck cardDeck;
    private DiscardDeck discardDeck;
    private List<RareFinds> rareFinds;
    private List<SimpleFinds> simpleFinds;
    private List<Murals> murals;

    private Card minotavro1;
    private Card minotavro2;

    private GraphicUI theView;


    /**
     * constructor for the controller,
     * creates the card deck, the discard deck, the 2 players, the board, and all the findings
     */
    public Controller() {
        initializeController();
    }

    private void initializeController(){
        board = new Board();
        rareFinds = new ArrayList<>();
        simpleFinds = new ArrayList<>();
        murals = new ArrayList<>();
        initializeFindings();
        List<Findings> findings = new ArrayList<>();
        findings.addAll(rareFinds);
        findings.addAll(simpleFinds);
        findings.addAll(murals);
        Collections.shuffle(findings, new Random(System.currentTimeMillis()));
        board.initializeBoard(findings);
        cardDeck = new CardDeck(board); // it is shuffled
        discardDeck = new DiscardDeck();
        player1 = new Player("Player1", RED, cardDeck, "project_assets/music/Player1.wav");
        player2 = new Player("Player2", GREEN, cardDeck, "project_assets/music/Player2.wav");
        music = new PlayersMusic(player1.getSoundPath(), player2.getSoundPath());
        minotavro1 = null;
        minotavro2 = null;
        startTurn();
    }

    public void setView(GraphicUI view) {
        this.theView = view;
        this.theView.addCardListener(new CardListener());
        this.theView.addCardDeckListener(new CardDeckListener());
        this.theView.addNewGameListener(new NewGameListener());
        this.theView.addExitGameListener(new ExitGameListener());
        this.theView.addSaveGameListener(new SaveGameListener());
        this.theView.addContinueSavedGameListener(new ContinueGameListener());
        this.theView.addMyMuralsButtonListener(new MuralsListener());
        this.theView.addCastlesLabelListener(new CastlesListener());
    }

    class CardListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            String command = button.getActionCommand();
            String[] parts = command.split("-");
            String player = parts[0]; // "player1" or "player2"
            int index = Integer.parseInt(parts[1]); // Index of the button
            Player playerP;
            if (player.equals("player1")) {
                playerP = player1;
            } else {
                playerP = player2;
            }
            if(!playerP.isTurn()){
                theView.displayErrorMessage("It's not your turn. you can't play a card. It's " + Controller.this.getCurrentPlayer().getName() + "'s turn.");
                return;
            }
            if(playerP.isPlayedCard()){
                theView.displayErrorMessage("You can't play a card because you already played one. Now pick a card from the Card deck");
                return;
            }
            if(e.getButton() == MouseEvent.BUTTON1) { // left click
                processCardEffect(playerP, index);
            }else if (e.getButton() == MouseEvent.BUTTON3){ // right click
                playerP.discardCard(index, discardDeck);
            }
            if(playerP == player1){
                minotavro2 = null;
            }else{
                minotavro1 = null;
            }
            updateUI(playerP, index);

            if(checkEndConditions()){
                endGame(getWinner());
            }

        }
    }

    class CardDeckListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = getCurrentPlayer();
            try{
                int index = player.drawCard(cardDeck);
                changeTurn();
                theView.makePawnsInvisible(getPlayerIndex(player), Controller.this);
                Player opponent = getOppositePlayer(player);
                theView.makePawnsVisible(getPlayerIndex(opponent), Controller.this);
                updateAfterCardDeck(player, index);
                checkForMinotavroFinding(opponent);
                updateUI(opponent, -1);
            } catch (IllegalMoveException ex) {
                theView.displayErrorMessage(ex.getMessage());
                return;
            }
            if(checkEndConditions()){
                endGame(getWinner());
            }
        }
    }

    class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            newGame();
        }
    }

    class ExitGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            exitGame();
        }
    }

    class SaveGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveGame();
        }
    }

    class ContinueGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            continueSavedGame();
        }
    }

    class MuralsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            Player player;
            if(command.equals("1")){
                player = player1;
            }else{
                player = player2;
            }
            try{
                openMyMuralsWindow(player);
            } catch (IllegalMoveException ex) {
                theView.displayErrorMessage(ex.getMessage());
            }

        }
    }

    class CastlesListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            String name = label.getName();
            int index = Integer.parseInt(name);
            Position lastPosition = Objects.requireNonNull(getPathFromIndex(index)).getPosition(9);
            String imagePath = lastPosition.getImagePath();
            int line = getLineInCsv(imagePath);
            showDialogForLine(line);
        }
    }



    /**
     * to make the needed changes when a card is played,
     * @param index of the card that was played
     * @param player that played the card
     * @post a card is played and effected the game
     */
    public void processCardEffect(Player player, int index){
        Player opponent = getOppositePlayer(player);
        Card card = player.getHand().get(index);
        if(card == null){
            theView.displayErrorMessage("there is no card there");
            return;
        }
        int pathIndex = getPathIndex(card.getPath(), null);
        Pawn pawn;
        if(card instanceof Minotavro){
            try{
                pawn = opponent.getPawns().get(pathIndex);
                if(pawn == null){
                    theView.displayErrorMessage("Can't play a Minotavro Card. There is no opponent pawn in the path");
                    return;
                }
                int prvPosition = pawn.getPositionNum();
                player.playCard(index, opponent);
                int nextPosition = pawn.getPositionNum();
                theView.movePawnFromToPosition(getPlayerIndex(opponent), pathIndex, prvPosition, nextPosition);
                theView.makePawnVisible(getPlayerIndex(opponent), this, pathIndex);
                if(player == player1){
                    minotavro1 = card;
                }else if (player == player2){
                    minotavro2 = card;
                }
            } catch (IllegalMoveException ex) {
                theView.displayErrorMessage(ex.getMessage());
                return;
            }
        }else{
            if(card instanceof SimpleCard){
                Path path = card.getPath();
                if(player.getPawns().get(pathIndex) == null){
                    String pawnS = theView.choosePawn();
                    while(pawnS != null){
                        if(pawnS.equals("arch")){
                            try{
                                player.createArch(path);
                                theView.addPawnToPlayer(getPlayerIndex(player), pathIndex, pawnS, Controller.this);
                                break;
                            } catch (IllegalMoveException ex) {
                                theView.displayErrorMessage(ex.getMessage());
                                pawnS = theView.choosePawn();
                            }
                        }else{
                            try{
                                player.createTheseus(path);
                                theView.addPawnToPlayer(getPlayerIndex(player), pathIndex, pawnS, Controller.this);
                                break;
                            } catch (IllegalMoveException ex) {
                                theView.displayErrorMessage(ex.getMessage());
                                pawnS = theView.choosePawn();
                            }
                        }
                    }if(pawnS == null){
                        return;
                    }
                }
            }
            try{
                pawn = player.getPawns().get(pathIndex);
                if(pawn == null && card instanceof Ariadni){
                    theView.displayErrorMessage("Cant throw an ariadni card at the start");
                    return;
                }if(checkTheseusMinotavro(player, card)){
                    theView.displayErrorMessage("Can't play the Card. Because " + getOppositePlayer(player).getName() + " previously just played a Minotavro card on your Theseus.");
                    return;
                }
                int prvPosition = pawn.getPositionNum();
                player.playCard(index, player);
                int nextPosition = pawn.getPositionNum();
                theView.movePawnFromToPosition(getPlayerIndex(player), pathIndex, prvPosition, nextPosition);
            } catch (IllegalMoveException ex) {
                theView.displayErrorMessage(ex.getMessage());
                return;
            }
            if(card instanceof Ariadni){
                Position prvPosition = getPreviousPosition(pawn);
                if(prvPosition != null){
                    try{
                        updateUI(player, index);
                        proccessFinding(player, pawn, prvPosition);
                    }catch(IllegalMoveException ex){
                        theView.displayErrorMessage(ex.getMessage());
                    }
                }
            }
            try{
                updateUI(player, index);
                proccessFinding(player, pawn, pawn.getPosition());
            } catch (IllegalMoveException e) {
                theView.displayErrorMessage(e.getMessage());
            }
        }
        if(player == player1){
            minotavro2 = null;
        }else{
            minotavro1 = null;
        }

    }

    public void proccessFinding(Player player, Pawn pawn, Position position) throws IllegalMoveException {
        if(position.hasFinds() && position.getOpenedBoxByPawn() != pawn){
            if(pawn instanceof Archaeologist){
                int choice = theView.openBox();
                if(choice == 1){
                    Findings findings = position.getFinds();
                    pawn.interactWithBox(position);
                    if(findings instanceof RareFinds){
                        theView.makeImageNotGray(getPlayerIndex(player), getPathIndex(pawn.getPath(), null), this);
                    }
                    int line = getLineInCsv(findings.getImagePath());
                    showDialogForLine(line);
                }
            }else if(pawn instanceof Theseus){
                int choice = theView.destroyBox();
                if(choice == 1){
                    pawn.interactWithBox(position);
                }
            }
        }
    }

    private void checkForMinotavroFinding(Player opponent){
        Card minotavro;
        if(opponent == player1){
            minotavro = minotavro2;
        }else{
            minotavro = minotavro1;
        }
        if(minotavro == null){
            return;
        }
        Path path = minotavro.getPath();
        int pathIndex = getPathIndex(path, null);
        Pawn pawn = opponent.getPawns().get(pathIndex);
        if(pawn == null || pawn instanceof Theseus){
            return;
        }
        try{
            proccessFinding(opponent, pawn, pawn.getPosition());
        }catch(IllegalMoveException ex){
            theView.displayErrorMessage(ex.getMessage());
        }

    }

    private Position getPreviousPosition(Pawn pawn){
        int prvPositionNum  = pawn.getPositionNum() - 1;
        Path path = pawn.getPath();
        for(int i = 1; i <= 9; i++){
            Position position = path.getPosition(i);
            if(position.getNumber() == prvPositionNum) return position;
        }
        return null;
    }



    private boolean checkTheseusMinotavro(Player player, Card card){
        // to do
        Card minotavro;
        if(player == player1){
            minotavro = minotavro2;
        }else{
            minotavro = minotavro1;
        }
        if(minotavro != null){
            if(card.getPath() == minotavro.getPath()){
                Path pathCard = minotavro.getPath();
                int pathIndex = getPathIndex(pathCard, null);
                Pawn pawn = player.getPawns().get(pathIndex);
                if(pawn != null){
                    if(pawn instanceof Theseus){
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Method updateUI updates the UI, but without the other player because no need, only change the other player's score beceuase it can change with a minotavro card.
     * Also it doesnt change the rareFinds when found (to make them ungray)
     * @param player that just recently played
     * @param cardIndex that just got played
     */
    private void updateUI(Player player, int cardIndex) {
        theView.setScoreLabel(1, player1.getScore());
        theView.setScoreLabel(2, player2.getScore());

        int availableTheseus;
        if(player.isTheseusAvailable()){
            availableTheseus = 1;
        }else{
            availableTheseus = 0;
        }
        theView.setAvailablePiecesLabel(getPlayerIndex(player), player.getAvailableArchaeologists(), availableTheseus);
        if(player.getLastCardKnossos() != null){
            theView.setLastCardOnPath(getPlayerIndex(player), 0, player.getLastCardKnossos().getImagePath());
        }
        if(player.getLastCardMalia() != null){
            theView.setLastCardOnPath(getPlayerIndex(player), 1, player.getLastCardMalia().getImagePath());
        }
        if(player.getLastCardPhaistos() != null){
            theView.setLastCardOnPath(getPlayerIndex(player), 2, player.getLastCardPhaistos().getImagePath());
        }
        if(player.getLastCardZakros() != null){
            theView.setLastCardOnPath(getPlayerIndex(player), 3, player.getLastCardZakros().getImagePath());
        }

        if(cardIndex != -1){
            updatePlayerCard(player, cardIndex);
        }
        theView.setSimpleFindsLabel(getPlayerIndex(player), player.getStatuettesCount());
        theView.updateInfoLabel(this);

    }

    private void updatePlayerCard(Player player, int cardIndex) {
        if(player.getHand().get(cardIndex) == null){
            theView.setPlayerCard(getPlayerIndex(player), cardIndex, null);
        }else{
            theView.setPlayerCard(getPlayerIndex(player), cardIndex, player.getHand().get(cardIndex).getImagePath());
        }
    }

    private void updateAfterCardDeck(Player player, int cardIndex) {
        updatePlayerCard(player, cardIndex);
        theView.updateInfoLabel(this);
    }


    /**
     * method initializeFindings initializes 20 findings :
     * creates 4 rare Finds
     * creates 10 Simple Finds
     * creates 6 Murals
     * put their imagePath, points for rare finds and murals, and the path for rare finds
     * and put them in the corresponding array list
     */
    private void initializeFindings(){
        RareFinds rarefinds1 = new RareFinds(board.getKnossos(), 25, "Ring of Minos", "project_assets/images/findings/ring.jpg");
        rareFinds.add(rarefinds1);
        RareFinds rarefinds2 = new RareFinds(board.getMalia(), 25, "Malia Jewel", "project_assets/images/findings/kosmima.jpg");
        rareFinds.add(rarefinds2);
        RareFinds rarefinds3 = new RareFinds(board.getPhaistos(), 35, "Phaistos Disk", "project_assets/images/findings/diskos.jpg");
        rareFinds.add(rarefinds3);
        RareFinds rarefinds4 = new RareFinds(board.getZakros(), 25, "Zakros Ryto", "project_assets/images/findings/ruto.jpg");
        rareFinds.add(rarefinds4);

        for(int i = 0; i < 10; i++){
            SimpleFinds simple = new SimpleFinds("project_assets/images/findings/snakes.jpg");
            simpleFinds.add(simple);
        }

        String path = "project_assets/images/findings/";
        String path1 = path + "fresco1_20.jpg";
        String path2 = path + "fresco2_20.jpg";
        String path3 = path + "fresco3_15.jpg";
        String path4 = path + "fresco4_20.jpg";
        String path5 = path + "fresco5_15.jpg";
        String path6 = path + "fresco6_15.jpg";
        Murals murals1 = new Murals(20, path1);
        Murals murals2 = new Murals(20, path2);
        Murals murals3 = new Murals(15, path3);
        Murals murals4 = new Murals(20, path4);
        Murals murals5 = new Murals(15, path5);
        Murals murals6 = new Murals(15, path6);
        murals.add(murals1);
        murals.add(murals4);
        murals.add(murals2);
        murals.add(murals5);
        murals.add(murals3);
        murals.add(murals6);
    }


    // do every method possible ...

    /**
     * method to start a new game
     * @post a new game is created
     */
    public void newGame(){

        if(theView != null){
            theView.dispose();
        }
        this.music.stopMusic();
        initializeController();

        GraphicUI gui = new GraphicUI(this);
        setView(gui);

    }

    /**
     * method to save the game
     * @post save the game somewhere so we can continue them
     */
    public void saveGame(){
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get("savedGame.ser")))) {
            out.writeObject(board);
            out.writeObject(player1);
            out.writeObject(player2);
            out.writeObject(cardDeck);
            out.writeObject(minotavro1);
            out.writeObject(minotavro2);
            JOptionPane.showMessageDialog(theView, "Game saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(theView, "Failed to save the game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * method to continue a saved game
     * @post continuing the game that was saved and exiting the one that we were playing
     */
    public void continueSavedGame(){

        java.nio.file.Path savedGamePath = Paths.get("savedGame.ser");

        if (!Files.exists(savedGamePath)) {
            JOptionPane.showMessageDialog(theView, "No saved game found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get("savedGame.ser")))) {
            theView.dispose();
            board = (Board) in.readObject();
            player1 = (Player) in.readObject();
            player2 = (Player) in.readObject();
            cardDeck = (CardDeck) in.readObject();
            minotavro1 = (Minotavro) in.readObject();
            minotavro2 = (Minotavro) in.readObject();

            theView = new GraphicUI(Controller.this);
            setView(theView);
            updateUI(player1, -1);
            updateUI(player2, -1);

            music.playMusicForPlayer(player1.isTurn() ? 1 : 2);

            for(int i = 0; i < 4; i++){
                Pawn pawn1 = player1.getPawns().get(i);
                if(pawn1 instanceof Archaeologist){
                    theView.addPawnToPlayer(1, getPathIndex(pawn1.getPath(), null), "arch", Controller.this);
                    theView.movePawnFromToPosition(1, getPathIndex(pawn1.getPath(), null), 1, pawn1.getPositionNum());
                }else if(pawn1 instanceof Theseus){
                    theView.addPawnToPlayer(1, getPathIndex(pawn1.getPath(), null), "theseus", Controller.this);
                    theView.movePawnFromToPosition(1, getPathIndex(pawn1.getPath(), null), 1, pawn1.getPositionNum());

                }

                Pawn pawn2 = player2.getPawns().get(i);
                if(pawn2 instanceof Archaeologist){
                    theView.addPawnToPlayer(2, getPathIndex(pawn2.getPath(), null), "arch", Controller.this);
                    theView.movePawnFromToPosition(2, getPathIndex(pawn2.getPath(), null), 1, pawn2.getPositionNum());

                }else if(pawn2 instanceof Theseus){
                    theView.addPawnToPlayer(2, getPathIndex(pawn2.getPath(), null), "theseus", Controller.this);
                    theView.movePawnFromToPosition(2, getPathIndex(pawn2.getPath(), null), 1, pawn2.getPositionNum());
                }

            }
            theView.makePawnsVisible(getPlayerIndex(getCurrentPlayer()), Controller.this);
            theView.makePawnsInvisible(getPlayerIndex(getOppositePlayer(getCurrentPlayer())), Controller.this);
            JOptionPane.showMessageDialog(theView, "Game loaded successfully!");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(theView, "Failed to load the game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * method to exit the game
     * @post there is no game
     */
    public void exitGame(){
        if (theView != null) {
            theView.dispose();
        }
        System.exit(0);
    }

    /**
     * method to start a game
     * @post a game is started
     */
    public void startGame(){

    }


    void openMyMuralsWindow(Player player) throws IllegalMoveException{
        if((player1.isTurn() && player == player2) || (player2.isTurn() && player == player1)){
            throw new IllegalMoveException("It is not your turn! You Can't open someone else's murals");
        }


        JFrame window = new JFrame("Murals for player" + getPlayerIndex(player));
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(450,400);
        window.setLocationRelativeTo(null);

        JPanel imagePanel = new JPanel(new GridLayout(3, 2, 0, 5)); // 2 rows, 3 columns
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon[] imageIcons = new ImageIcon[6];

        List<Murals> playerMurals = player.getMurals();
        for(int i = 0; i < 6; i++){
            for(Murals mural : playerMurals){
                if(mural != murals.get(i)) continue;
                imageIcons[i] = new ImageIcon(murals.get(i).getImagePath());
            }
            if(imageIcons[i] == null){
                imageIcons[i] = new ImageIcon(murals.get(i).getImagePath());
                Image gray = theView.makeImageGray(imageIcons[i].getImage());
                imageIcons[i].setImage(gray);
            }

            JLabel imageLabel = new JLabel(imageIcons[i]);
            imagePanel.add(imageLabel);
        }



        window.add(imagePanel);
        window.setVisible(true);
    }

    public void showDialogForLine(int lineNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader("project_assets/csvFiles/csv_greek.csv"))) {
            String line;
            int currentLine = 0;

            while ((line = br.readLine()) != null) {
                currentLine++;

                if (currentLine == lineNumber) {
                    String[] parts = line.split(";", 3);


                    String imagePath = "project_assets/images/" + parts[0].trim();
                    String message = parts[1].trim();
                    String description = parts[2].trim();

                    ImageIcon icon = new ImageIcon(imagePath);

                    JLabel descriptionLabel = new JLabel("<html><p style='width:400px;font-size:12px;'>" + description + "</p></html>");

                    JOptionPane.showMessageDialog(
                            null,
                            descriptionLabel,
                            message,
                            JOptionPane.INFORMATION_MESSAGE,
                            icon
                    );
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error reading the file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JOptionPane.showMessageDialog(
                null,
                "Line number " + lineNumber + " not found in the file.",
                "Not Found",
                JOptionPane.WARNING_MESSAGE
        );
    }

    int getLineInCsv(String imagePath){
        String[] paths = new String[15];
        String s = "project_assets/images/";
        paths[0] = s + "findings/diskos.jpg";
        paths[1] = s + "findings/kosmima.jpg";
        paths[2] = s + "findings/ruto.jpg";
        paths[3] = s + "findings/ring.jpg";
        paths[4] = s + "findings/snakes.jpg";
        paths[5] = s + "findings/fresco1_20.jpg";
        paths[6] = s + "findings/fresco2_20.jpg";
        paths[7] = s + "findings/fresco3_15.jpg";
        paths[8] = s + "findings/fresco4_20.jpg";
        paths[9] = s + "findings/fresco5_15.jpg";
        paths[10] = s + "findings/fresco6_15.jpg";
        paths[11] = s + "paths/knossosPalace.jpg";
        paths[12] = s + "paths/phaistosPalace.jpg";
        paths[13] = s + "paths/maliaPalace.jpg";
        paths[14] = s + "paths/zakrosPalace.jpg";
        for(int i = 0; i < paths.length; i++){
            if(imagePath.equals(paths[i])) return (i + 2);
        }
        return -1;
    }

    /**
     * method to know which player has the turn
     * @return the player with the turn
     */
    public Player getCurrentPlayer(){
        if(player1.isTurn() == true) return player1;
        else{
            return player2;
        }
    }

    /**
     * make a method that with some choose the first player to start
     */
    public void startTurn(){
        Random random = new Random();
        int randomNumber = random.nextInt(2) + 1;
        if(randomNumber == 1){
            player1.setTurn(true);
            player2.setTurn(false);
        }else{
            player2.setTurn(true);
            player1.setTurn(false);
        }
        music.playMusicForPlayer(randomNumber);
    }


    /**
     * accessor(selector) :Returns which player has the turn
     * Postcondition: Returns which player has the turn
     *
     * @return the player that has the turn
     */
    public Player getTurn()
    {
        return getCurrentPlayer();
    }

    /**
     * method to change the turn using the method currentPlayer,
     * if currentPlayer is Player1 then -> Player2.setTurn(true) and Player1.setTurn(false)
     */
    public void changeTurn(){
        if(player1.isTurn()){
            player1.setTurn(false);
            player2.setTurn(true);

            music.playMusicForPlayer(2);
        }else{
            player2.setTurn(false);
            player1.setTurn(true);

            music.playMusicForPlayer(1);
        }
    }

    /**
     * method to always check if there are any ending conditions.
     * ending conditions :
     * 1- CardDeck is empty,
     * 2- 4 pawns are reached or have exceeded the position 7 (checkpoint)
     * @return true if one of the ending conditions is ture
     */
    public boolean checkEndConditions(){
        if(cardDeck.isEmpty()) return true;
        int count = 0;
        for(Pawn pawn : player1.getPawns()){
            if(pawn != null){
                if(pawn.getPositionNum() >= 7){
                    count++;
                }
            }
        }
        for(Pawn pawn : player2.getPawns()){
            if(pawn != null){
                if(pawn.getPositionNum() >= 7){
                    count++;
                }
            }
        }
            if(count == 4){
                return true;
            }
            return false;

    }


    /**
     * check if the pawn is on a position that has findings
     * @param pawn that we want to check if its position has finds
     */
    public boolean onFinding(Pawn pawn){
        return pawn.getPosition().hasFinds();
    }

    /**
     * bonus method to check the timer of a player
     * and to end its turn when the timer ends
     * @param player that has the turn
     */
    public void timer(Player player){

    }

    /**
     * method that calculates the points of each player and returns the player that wins
     * @pre checkEndConditions is true
     * @post we know which player won
     * @return the player with the most points, and null if they are equal
     */
    public Player getWinner(){
        if(player1.getScore() > player2.getScore()){
            return player1;
        }else if(player2.getScore() > player1.getScore()){
            return player2;
        }else{
            int rareFinds1 = 0;
            for(RareFinds rareFinds : player1.getRareFinds()){
                if(rareFinds != null){
                    rareFinds1++;
                }
            }
            int rareFinds2 = 0;
            for(RareFinds rareFinds : player2.getRareFinds()){
                if(rareFinds != null){
                    rareFinds2++;
                }
            }
            if(rareFinds1 > rareFinds2){
                return player1;
            }else if(rareFinds2 > rareFinds1){
                return player2;
            }else{
                int murals1 = player1.getMurals().size();
                int murals2 = player2.getMurals().size();
                if(murals1 > murals2){
                    return player1;
                }else if(murals2 > murals1){
                    return player2;
                }else{
                    int statuettesCount1 = player1.getStatuettesCount();
                    int statuettesCount2 = player2.getStatuettesCount();
                    if(statuettesCount1 > statuettesCount2){
                        return player1;
                    }else if(statuettesCount2 > statuettesCount1){
                        return player2;
                    }else{
                        return null;
                    }
                }
            }
        }

    }

    void endGame(Player player){
        String string;
        if(player == player1){
            string = "Player1 won!";
        }else if(player == player2){
            string = "Player2 won!";
        }else{
            string = "It is a tie";
        }
        int choice = theView.endGame(string);
        if(choice == 1){
            newGame();
        }
    }

    // getters for all the variables of the game

    /**
     * Accessor of player1
     * @return player1
     */
    public Player getPlayer1(){
        return player1;
    }

    /**
     * Accessor of player2
     * @return player2
     */
    public Player getPlayer2(){
        return player2;
    }

    /**
     * method to return the opposite player. This will be used when a minotavro card is thrown
     * @param player that we want to know the other player
     * @return the opposite player
     */
    public Player getOppositePlayer(Player player){
        if(player == player1) return player2;
        else return player1;
    }


    /**
     * Accessor of the players score
     * @param player that i want to get the score of
     * @return the score of the player
     */
    public int getScore(Player player){
        return player.getScore();
    }

    /**
     * Accessor of the players statuettes Count
     * @param player that we want to check its statuettes count
     * @return the player's statuettes count
     */
    public int getStatuettesCount(Player player){
        return player.getStatuettesCount();
    }

    /**
     * Accessor for the player's rare finds
     * @param player that we want to access its rare finds
     * @return the rare finds of the player
     */
    public List<RareFinds> getRareFinds(Player player) {
        return player.getRareFinds();
    }

    /**
     * Accessor of the murals that the player have
     * @param player that we want to access its murals
     * @return the player's murals
     */
    public List<Murals> getMurals(Player player) {
        return player.getMurals();
    }

    /**
     * Accessor of the points of the position
     * @param position that we want to check its poits
     * @return the points of the position
     */
    public int getPositionPoints(Position position){
        return position.getPoints();
    }

    /**
     * Accessor of the board
     * @return the board of the game
     */
    public Board getBoard(){
        return board;
    }

    /**
     * accessor of the card deck
     * @return the card deck of the game
     */
    public CardDeck getCardDeck(){
        return cardDeck;
    }

    /**
     * Accessor : to get the amount of available cards
     * @return the number of available cards left
     */
    public int getAvailableCards(){
        return cardDeck.getSize();
    }

    /**
     * accessor of the discard deck
     * @return the discard deck
     */
    public DiscardDeck getDiscardDeck(){
        return discardDeck;
    }

    /**
     * accessor of the rare finds of the game
     * @return the rare finds
     */
    public List<RareFinds> getRareFinds(){
        return rareFinds;
    }

    /**
     * accessor of the simple finds
     * @return the simple finds
     */
    public List<SimpleFinds> getSimpleFinds(){
        return simpleFinds;
    }

    /**
     * accessor of the murals
     * @return the murals of the game
     */
    public List<Murals> getMurals(){
        return murals;
    }

    /**
     * accessor of the check points
     * @return how many pawns passed the check point mark
     */
    public int getCheckPoints(){
        int checkPoints = 0;
        for(Pawn pawn : player1.getPawns()){
            if(pawn != null){
                if(pawn.getPositionNum() >= 7) checkPoints++;
            }
        }
        for(Pawn pawn : player2.getPawns()){
            if(pawn != null){
                if(pawn.getPositionNum() >= 7) checkPoints++;
            }
        }
        return checkPoints;
    }

    // from 0 to 3
    public int getPathIndex(Path path, String pathName){
        if(path == board.getKnossos() || Objects.equals(pathName, board.getKnossos().getName())) return 0;
        else if(path == board.getMalia() || Objects.equals(pathName, board.getMalia().getName())) return 1;
        else if(path == board.getPhaistos() || Objects.equals(pathName, board.getPhaistos().getName())) return 2;
        else if(path == board.getZakros() || Objects.equals(pathName, board.getZakros().getName())) return 3;
        return -1;
    }

    //from 0 to 3
    private Path getPathFromIndex(int index){
        if(index == 0) return board.getKnossos();
        else if(index == 1) return board.getMalia();
        else if(index == 2) return board.getPhaistos();
        else if(index == 3) return board.getZakros();
        return null;
    }

    private int getPlayerIndex(Player player){
        if(player == player1) return 1;
        else if(player == player2) return 2;
        return 0;
    }

}


