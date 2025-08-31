package mvc.view;

import mvc.controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

/**
 * @author Elias Rishmawi
 */

/**
 * GraphicUI class
 */
public class GraphicUI extends JFrame {

    private ImageIcon backgroundImage;
    private JMenuBar menuBar;
    private JMenuItem newGameMenuItem;
    private JMenuItem saveGameMenuItem;
    private JMenuItem continueGameMenuItem;
    private JMenuItem exitGameMenuItem;

    private JLayeredPane gameBoard;
    private JPanel pathBoard;
    private JPanel pointsPanel;
    private JPanel lastColumnPanel;
    private JPanel deckBoard;
    private JButton cardDeckButton;
    private JLabel[] path1Labels;
    private JLabel[] path2Labels;
    private JLabel[] path3Labels;
    private JLabel[] path4Labels;
    private JLabel[] pointsLabels;
    private JLabel infoLabel;
    private ImageIcon cardDeckIcon;


    private JLayeredPane player1LayeredPane;
    private JButton[] player1Cards;
    private JLabel[] lastCardOnPathLabels1;
    private JLabel availablePiecesLabel1;
    private JLabel scoreLabel1;
    private JLabel simpleFindsLabel1;
    private JButton myMuralsButton1;
    private JLabel[] pawnLabel1;
    private JLabel[] rareFinds1;


    private JLayeredPane player2LayeredPane;
    private JButton[] player2Cards;
    private JLabel[] lastCardOnPathLabels2;
    private JLabel availablePiecesLabel2;
    private JLabel scoreLabel2;
    private JLabel simpleFindsLabel2;
    private JButton myMuralsButton2;
    private JLabel[] pawnLabel2;
    private JLabel[] rareFinds2;

    private JOptionPane excavationMessageDialog;
    /**
     * <b>constructor</b>: Initializes the main window and its components.<br />
     * <b>postconditions</b>: A new window is created with all components laid out.
     */
    public GraphicUI(Controller controller) {
        initComponents(controller);
    }

    /**
     * <b>transformer(mutative)</b>: Initializes all UI components and their layout.<br />
     * <p><b>Postcondition:</b> All UI components are initialized and arranged.</p>
     */
    private void initComponents(Controller controller) {
        setTitle("Αναζητώντας το Χαμένο Μινωικό Ανάκτορο");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1000);
        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        newGameMenuItem = new JMenuItem("New Game");
        saveGameMenuItem = new JMenuItem("Save Game");
        continueGameMenuItem = new JMenuItem("Continue Saved Game");
        exitGameMenuItem = new JMenuItem("Exit Game");

        JMenu newGameMenu = new JMenu("New Game");
        JMenu saveGameMenu = new JMenu("Save Game");
        JMenu continueGameMenu = new JMenu("Continue Saved Game");
        JMenu exitGameMenu = new JMenu("Exit Game");

        newGameMenu.add(newGameMenuItem);
        saveGameMenu.add(saveGameMenuItem);
        continueGameMenu.add(continueGameMenuItem);
        exitGameMenu.add(exitGameMenuItem);

        menuBar.add(newGameMenu);
        menuBar.add(saveGameMenu);
        menuBar.add(continueGameMenu);
        menuBar.add(exitGameMenu);
        setJMenuBar(menuBar);

        gameBoard = new JLayeredPane();
        gameBoard.setBounds(0, 221, 1920, 560);
        pathBoard = new JPanel(new GridLayout(4, 8, 10, 25));
        pathBoard.setBounds(600, 50, 960, 430);
        pointsPanel = new JPanel(new GridLayout(1, 9, 10, 15));
        pointsPanel.setBounds(600, 13, 1080, 40);
        lastColumnPanel = new JPanel(new GridLayout(4, 1, 15, 1));
        lastColumnPanel.setBounds(1570, 38, 150, 457);
        deckBoard = new JPanel();
        deckBoard.setBounds(150, 200, 200, 300);

        cardDeckIcon = new ImageIcon("project_assets/images/cards/backCard.jpg");
        Image cardDeck = cardDeckIcon.getImage().getScaledInstance(115, 150, Image.SCALE_SMOOTH);
        cardDeckIcon = new ImageIcon(cardDeck);
        cardDeckButton = new JButton(cardDeckIcon);
        cardDeckButton.setPreferredSize(new Dimension(115, 150));
        infoLabel = new JLabel("<html>Available Cards: " + controller.getAvailableCards() +
                "<br>Check Points: " + controller.getCheckPoints() +
                "<br>Turn: " + controller.getTurn().getName() + "</html>");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 17));
        infoLabel.setBorder(new LineBorder(Color.black, 2));

        deckBoard.add(cardDeckButton);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        deckBoard.add(infoLabel);


        path1Labels = createPathLabels(controller.getBoard().getKnossos().getName(), controller);
        path2Labels = createPathLabels(controller.getBoard().getMalia().getName(), controller);
        path3Labels = createPathLabels(controller.getBoard().getPhaistos().getName(), controller);
        path4Labels = createPathLabels(controller.getBoard().getZakros().getName(), controller);
        pointsLabels = new JLabel[9];
        for(int i = 0; i < pointsLabels.length; i++){
            if(i == 6){
                pointsLabels[i] = new JLabel("<html>" + String.valueOf(controller.getBoard().getKnossos().getPosition(i+1).getPoints()) + " points" + "<br>Check Point!</html>");
            }else{
                pointsLabels[i] = new JLabel(String.valueOf(controller.getBoard().getKnossos().getPosition(i+1).getPoints()) + " points");
            }
            pointsLabels[i].setFont(new Font("Arial", Font.BOLD, 17));
            pointsLabels[i].setVerticalAlignment(SwingConstants.TOP);
        }

        for(JLabel pointLabel : pointsLabels){
            pointsPanel.add(pointLabel);
        }
        for (int i = 0; i < 8; i++) {
            pathBoard.add(path1Labels[i]);
        }
        for (int i = 0; i < 8; i++) {
            pathBoard.add(path2Labels[i]);
        }
        for (int i = 0; i < 8; i++) {
            pathBoard.add(path3Labels[i]);
        }
        for (int i = 0; i < 8; i++) {
            pathBoard.add(path4Labels[i]);
        }

        lastColumnPanel.add(path1Labels[8]);
        lastColumnPanel.add(path2Labels[8]);
        lastColumnPanel.add(path3Labels[8]);
        lastColumnPanel.add(path4Labels[8]);

        backgroundImage = new ImageIcon("project_assets/images/background.jpg");
        Image img = backgroundImage.getImage().getScaledInstance(gameBoard.getWidth(), gameBoard.getHeight(), Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(img);
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
        gameBoard.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        deckBoard.setOpaque(false);
        gameBoard.add(deckBoard, JLayeredPane.PALETTE_LAYER);

        pathBoard.setOpaque(false);
        gameBoard.add(pathBoard, JLayeredPane.PALETTE_LAYER);

        lastColumnPanel.setOpaque(false);
        gameBoard.add(lastColumnPanel, JLayeredPane.PALETTE_LAYER);

        pointsPanel.setOpaque(false);
        gameBoard.add(pointsPanel, JLayeredPane.PALETTE_LAYER);

        player1LayeredPane = createPlayerLayeredPane(controller.getPlayer1().getName(), controller.getPlayer1().getColor(), controller);
        player2LayeredPane = createPlayerLayeredPane(controller.getPlayer2().getName(), controller.getPlayer2().getColor(), controller);

        /*addPawnToPlayer(1, 0, "arch", controller);
        controller.getPlayer1().createArch(controller.getBoard().getKnossos());
        addPawnToPlayer(2, 0, "arch", controller);
        controller.getPlayer2().createArch(controller.getBoard().getKnossos());
        movePawnFromToPosition(1, 0, 1, 2);
        movePawnFromToPosition(2, 0, 1, 9);
        makeImageNotGray(2, 4, controller);
        setScoreLabel(2, 200);
        setSimpleFindsLabel(1, 2);
        setLastCardOnPath(2, 0, "project_assets/images/cards/knossos1.jpg");
        setPlayerCard(1, 0, null);
        makePawnsInvisible(2, controller);
        makePawnsVisible(2, controller);
        updateInfoLabel(controller);*/


        add(player1LayeredPane, BorderLayout.NORTH);
        add(gameBoard, BorderLayout.CENTER);
        add(player2LayeredPane, BorderLayout.SOUTH);

        setVisible(true);

    }


    /**
     * <b>transformer(mutative)</b>: Creates a player's layered pane with cards and labels.<br />
     * <p><b>Postcondition:</b> A player's layered pane is returned.</p>
     *
     * @param playerName  the name of the player
     * @param borderColor the color of the border for the pane
     * @return a layered pane representing the player's area
     */
    private JLayeredPane createPlayerLayeredPane(String playerName, Color borderColor, Controller controller) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1920, 220));
        layeredPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor), playerName));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 8, 5, 5));
        JPanel lastPlayedCardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        JPanel rareFindsPanel = new JPanel(new GridLayout(1, 4, 76, 76));
        JPanel playerInfo = new JPanel(new GridLayout(3, 1, 20, 20));
        JPanel availablePawnsPanel = new JPanel();

        cardsPanel.setBounds(20, 20, 955, 150);
        lastPlayedCardsPanel.setBounds(1020, 20, 520, 150);
        rareFindsPanel.setBounds(1047, 170, 468, 45);
        playerInfo.setBounds(1600, 30, 250, 170);
        availablePawnsPanel.setBounds(20, 180, 450, 30);

        String[] pathNames = {"Knossos", "Malia", "Phaistos", "Zakros"};
        ImageIcon sIcon = new ImageIcon("project_assets/images/findings/snakes.jpg");
        Image s = sIcon.getImage().getScaledInstance(40, 45, Image.SCALE_SMOOTH);
        ImageIcon statuettes = new ImageIcon(s);

        if(Objects.equals(playerName, "Player1")){
            pawnLabel1 = new JLabel[4];
            player1Cards = new JButton[8];
            for (int i = 0; i < 8; i++) {
                String path;
                if(controller.getPlayer1().getHand().get(i) == null){
                    path = null;
                }else{
                    path = controller.getPlayer1().getHand().get(i).getImagePath();
                }
                ImageIcon card = new ImageIcon(path);
                Image img = card.getImage().getScaledInstance(115, 150, Image.SCALE_SMOOTH);
                ImageIcon scaledCard = new ImageIcon(img);
                player1Cards[i] = new JButton(scaledCard);
                player1Cards[i].setBorder(BorderFactory.createEmptyBorder());
                player1Cards[i].setPreferredSize(new Dimension(115, 150));
                cardsPanel.add(player1Cards[i]);
            }
            lastCardOnPathLabels1 = new JLabel[4];
            for(int i = 0; i < lastCardOnPathLabels1.length; i++){
                lastCardOnPathLabels1[i] = new JLabel(pathNames[i]);
                if(pathNames[i].equals("Knossos")){
                    lastCardOnPathLabels1[i].setBorder(new LineBorder(controller.getBoard().getKnossos().getColor(), 3));
                }else if(pathNames[i].equals("Malia")){
                    lastCardOnPathLabels1[i].setBorder(new LineBorder(controller.getBoard().getMalia().getColor(), 3));
                }else if(pathNames[i].equals("Phaistos")){
                    lastCardOnPathLabels1[i].setBorder(new LineBorder(controller.getBoard().getPhaistos().getColor(), 3));
                }else if(pathNames[i].equals("Zakros")){
                    lastCardOnPathLabels1[i].setBorder(new LineBorder(controller.getBoard().getZakros().getColor(), 3));
                }
                lastCardOnPathLabels1[i].setPreferredSize(new Dimension(115, 150));
                lastCardOnPathLabels1[i].setFont(new Font("Arial", Font.BOLD, 15));
                lastPlayedCardsPanel.add(lastCardOnPathLabels1[i]);
            }
            rareFinds1 = new JLabel[4];
            for(int i = 0; i < rareFinds1.length; i++){
                ImageIcon card = new ImageIcon(controller.getRareFinds().get(i).getImagePath());
                Image img = card.getImage().getScaledInstance(60, 50, Image.SCALE_SMOOTH);
                Image gray = makeImageGray(img);
                ImageIcon scaledCard = new ImageIcon(gray);
                rareFinds1[i] = new JLabel(scaledCard);
                rareFinds1[i].setPreferredSize(new Dimension(60, 50));
                rareFindsPanel.add(rareFinds1[i]);
            }
            this.scoreLabel1 = new JLabel("My score: " + controller.getPlayer1().getScore() + " Points");
            this.myMuralsButton1 = new JButton("My Murals");
            this.simpleFindsLabel1 = new JLabel("Statuettes: " + controller.getPlayer1().getStatuettesCount(), statuettes, JLabel.LEFT);
            simpleFindsLabel1.setHorizontalTextPosition(SwingConstants.LEFT);
            simpleFindsLabel1.setIconTextGap(15);
            playerInfo.add(scoreLabel1, BorderLayout.NORTH);
            playerInfo.add(myMuralsButton1, BorderLayout.CENTER);
            playerInfo.add(simpleFindsLabel1, BorderLayout.SOUTH);
            String theseusAvailable;
            if(controller.getPlayer1().getTheseus() == null){
                theseusAvailable = "1";
            }else{
                theseusAvailable = "0";
            }
            int availableArch = 3 - controller.getPlayer1().getArchaeologists().size();
            this.availablePiecesLabel1 = new JLabel(controller.getPlayer1().getName() + " -Available Pawns: " + availableArch + " Archaeologists and " + theseusAvailable + " Theseus");
            availablePawnsPanel.add(availablePiecesLabel1);
        }else if(Objects.equals(playerName, "Player2")){
            pawnLabel2 = new JLabel[4];
            player2Cards = new JButton[8];
            for (int i = 0; i < 8; i++) {
                String path;
                if(controller.getPlayer2().getHand().get(i) == null){
                    path = null;
                }else{
                    path = controller.getPlayer2().getHand().get(i).getImagePath();
                }
                ImageIcon card = new ImageIcon(path);
                Image img = card.getImage().getScaledInstance(115, 150, Image.SCALE_SMOOTH);
                ImageIcon scaledCard = new ImageIcon(img);
                player2Cards[i] = new JButton(scaledCard);
                player2Cards[i].setBorder(BorderFactory.createEmptyBorder());
                player2Cards[i].setPreferredSize(new Dimension(115, 150));
                cardsPanel.add(player2Cards[i]);
            }
            lastCardOnPathLabels2 = new JLabel[4];
            for(int i = 0; i < lastCardOnPathLabels2.length; i++){
                lastCardOnPathLabels2[i] = new JLabel(pathNames[i]);
                if(pathNames[i].equals("Knossos")){
                    lastCardOnPathLabels2[i].setBorder(new LineBorder(controller.getBoard().getKnossos().getColor(), 3));
                }else if(pathNames[i].equals("Malia")){
                    lastCardOnPathLabels2[i].setBorder(new LineBorder(controller.getBoard().getMalia().getColor(), 3));
                }else if(pathNames[i].equals("Phaistos")){
                    lastCardOnPathLabels2[i].setBorder(new LineBorder(controller.getBoard().getPhaistos().getColor(), 3));
                }else if(pathNames[i].equals("Zakros")){
                    lastCardOnPathLabels2[i].setBorder(new LineBorder(controller.getBoard().getZakros().getColor(), 3));
                }
                lastCardOnPathLabels2[i].setPreferredSize(new Dimension(115, 150));
                lastCardOnPathLabels2[i].setFont(new Font("Arial", Font.BOLD, 15));
                lastPlayedCardsPanel.add(lastCardOnPathLabels2[i]);
            }
            rareFinds2 = new JLabel[4];
            for(int i = 0; i < rareFinds2.length; i++){
                ImageIcon card = new ImageIcon(controller.getRareFinds().get(i).getImagePath());
                Image img = card.getImage().getScaledInstance(60, 50, Image.SCALE_SMOOTH);
                Image gray = makeImageGray(img);
                ImageIcon scaledCard = new ImageIcon(gray);
                rareFinds2[i] = new JLabel(scaledCard);
                rareFinds2[i].setPreferredSize(new Dimension(60, 50));
                rareFindsPanel.add(rareFinds2[i]);
            }
            this.scoreLabel2 = new JLabel("My score: " + controller.getPlayer2().getScore() + " Points");
            this.myMuralsButton2 = new JButton("My Murals");
            this.simpleFindsLabel2 = new JLabel("Statuettes: " + controller.getPlayer2().getStatuettesCount(), statuettes, JLabel.LEFT);
            simpleFindsLabel2.setHorizontalTextPosition(SwingConstants.LEFT);
            simpleFindsLabel2.setIconTextGap(15);
            playerInfo.add(scoreLabel2, BorderLayout.NORTH);
            playerInfo.add(myMuralsButton2, BorderLayout.CENTER);
            playerInfo.add(simpleFindsLabel2, BorderLayout.SOUTH);
            String theseusAvailable;
            if(controller.getPlayer2().getTheseus() == null){
                theseusAvailable = "1";
            }else{
                theseusAvailable = "0";
            }
            int availableArch = 3 - controller.getPlayer2().getArchaeologists().size();
            this.availablePiecesLabel2 = new JLabel(controller.getPlayer2().getName() + " -Available Pawns: " + availableArch + " Archaeologists and " + theseusAvailable + " Theseus");
            availablePawnsPanel.add(availablePiecesLabel2);
        }

        availablePawnsPanel.getComponent(0).setFont(new Font("Arial", Font.BOLD, 16));
        playerInfo.getComponent(0).setFont(new Font("Arial", Font.BOLD, 16));
        playerInfo.getComponent(1).setPreferredSize(new Dimension(200, 20));
        playerInfo.getComponent(1).setFont(new Font("Arial", Font.BOLD, 15));
        playerInfo.getComponent(2).setFont(new Font("Arial", Font.BOLD, 16));

        layeredPane.add(cardsPanel);
        layeredPane.add(lastPlayedCardsPanel);
        layeredPane.add(rareFindsPanel);
        layeredPane.add(playerInfo);
        layeredPane.add(availablePawnsPanel);
        return layeredPane;
    }

    /**
     * <b>transformer(mutative)</b>: Creates JLabels for paths on the game board.<br />
     * <p><b>Postcondition:</b> Path labels are returned as an array.</p>
     *
     * @return an array of JLabels representing the paths
     */
    private JLabel[] createPathLabels(String name, Controller controller) {

        JLabel[] labels = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            ImageIcon image;
            if(Objects.equals(name, "Knossos")){
                image = new ImageIcon(controller.getBoard().getKnossos().getPosition(i+1).getImagePath());
            }else if(Objects.equals(name, "Malia")){
                image = new ImageIcon(controller.getBoard().getMalia().getPosition(i+1).getImagePath());
            }else if(Objects.equals(name, "Phaistos")){
                image = new ImageIcon(controller.getBoard().getPhaistos().getPosition(i+1).getImagePath());
            }else {
                image = new ImageIcon(controller.getBoard().getZakros().getPosition(i+1).getImagePath());
            }
            if(i != 8){
                Image img = image.getImage().getScaledInstance(115, 90, Image.SCALE_SMOOTH);
                ImageIcon imageScaled = new ImageIcon(img);
                labels[i] = new JLabel(imageScaled);
                labels[i].setPreferredSize(new Dimension(125, 100));
            }else{
                Image img = image.getImage().getScaledInstance(160, 112, Image.SCALE_SMOOTH);
                ImageIcon imageScaled = new ImageIcon(img);
                labels[i] = new JLabel(imageScaled);
                labels[i].setPreferredSize(new Dimension(170, 128));
            }
            labels[i].setLayout(new FlowLayout(FlowLayout.LEFT, 8, 10));

        }
        return labels;
    }

    public static Image makeImageGray(Image img) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = (rgb) & 0xff;

                int gray = (red + green + blue) / 3;

                gray = (int) (gray * 0.7);

                return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
            }
        };

        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(img.getSource(), filter));
    }

    public void setAvailablePiecesLabel(int player, int arch, int theseus){
        String string = "Player" + player + " -Available Pawns: " + arch + " Archaeologists and " + theseus + " Theseus";
        if(player == 1){
            availablePiecesLabel1.setText(string);
        }else{
            availablePiecesLabel2.setText(string);
        }
    }

    public void setScoreLabel(int player, int score){
        String string = "My score: " + score + " Points";
        if(player == 1){
            scoreLabel1.setText(string);
        }else{
            scoreLabel2.setText(string);
        }
    }

    public void setSimpleFindsLabel(int player, int simpleFinds){
        if(player == 1){
            simpleFindsLabel1.setText("Statuettes: " + simpleFinds);
        }else{
            simpleFindsLabel2.setText("Statuettes: " + simpleFinds);
        }
    }

    public void setLastCardOnPath(int player, int path, String imagePath){
        ImageIcon card = new ImageIcon(imagePath);
        Image img = card.getImage().getScaledInstance(115, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledCard = new ImageIcon(img);
        if(player == 1){
            lastCardOnPathLabels1[path].setIcon(scaledCard);
        }else{
            lastCardOnPathLabels2[path].setIcon(scaledCard);
        }
    }

    public void setPlayerCard(int player, int cardIndex, String imagePath){
        if(imagePath == null){
            if(player == 1){
                player1Cards[cardIndex].setIcon(null);
            }else{
                player2Cards[cardIndex].setIcon(null);
            }
            return;
        }
        ImageIcon card = new ImageIcon(imagePath);
        Image img = card.getImage().getScaledInstance(115, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledCard = new ImageIcon(img);
        if(player == 1){
            player1Cards[cardIndex].setIcon(scaledCard);
        }else{
            player2Cards[cardIndex].setIcon(scaledCard);
        }
    }

    public void addPawnToPlayer(int player, int path, String pawn, Controller controller){
        String imagePath = "project_assets/images/pionia/" + pawn + ".jpg";
        ImageIcon pawnIcon = new ImageIcon(imagePath);
        Image img = pawnIcon.getImage().getScaledInstance(35, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledPawn = new ImageIcon(img);
        if(player == 1){
            pawnLabel1[path] = new JLabel(scaledPawn);
            pawnLabel1[path].setBorder(new LineBorder(controller.getPlayer1().getColor(), 4));
            if(path == 0){
                path1Labels[0].add(pawnLabel1[path]);
                path1Labels[0].revalidate();
                path1Labels[0].repaint();
            }else if(path == 1){
                path2Labels[0].add(pawnLabel1[path]);
                path2Labels[0].revalidate();
                path2Labels[0].repaint();
            }else if(path == 2){
                path3Labels[0].add(pawnLabel1[path]);
                path3Labels[0].revalidate();
                path3Labels[0].repaint();
            }else{
                path4Labels[0].add(pawnLabel1[path]);
                path4Labels[0].revalidate();
                path4Labels[0].repaint();
            }
            pawnLabel1[path].setVisible(true);
        }else{
            pawnLabel2[path] = new JLabel(scaledPawn);
            pawnLabel2[path].setBorder(new LineBorder(controller.getPlayer2().getColor(), 4));
            if(path == 0){
                path1Labels[0].add(pawnLabel2[path]);
                path1Labels[0].revalidate();
                path1Labels[0].repaint();
            }else if(path == 1){
                path2Labels[0].add(pawnLabel2[path]);
                path2Labels[0].revalidate();
                path2Labels[0].repaint();
            }else if(path == 2){
                path3Labels[0].add(pawnLabel2[path]);
                path3Labels[0].revalidate();
                path3Labels[0].repaint();
            }else{
                path4Labels[0].add(pawnLabel2[path]);
                path4Labels[0].revalidate();
                path4Labels[0].repaint();
            }
            pawnLabel2[path].setVisible(true);

        }

    }

    public void makePawnsInvisible(int player, Controller controller){
        for(int i = 0 ; i < 4; i++){
            if(player == 1){
                if(controller.getPlayer1().getPawns().get(i) == null) continue;
                else if(controller.getPlayer1().getPawns().get(i).isVisible()){
                    pawnLabel1[i].setVisible(true);
                }
                else{
                    pawnLabel1[i].setVisible(false);
                }
            }else{
                if(controller.getPlayer2().getPawns().get(i) == null) continue;
                else if(controller.getPlayer2().getPawns().get(i).isVisible()){
                    pawnLabel2[i].setVisible(true);
                }
                else{
                    pawnLabel2[i].setVisible(false);
                }
            }
        }
    }

    public void makePawnsVisible(int player, Controller controller){
        for(int i = 0 ; i < 4; i++){
            if(player == 1){
                if(controller.getPlayer1().getPawns().get(i) == null) continue;
                else{
                    pawnLabel1[i].setVisible(true);
                }
            }
            else{
                if(controller.getPlayer2().getPawns().get(i) == null) continue;
                else{
                    pawnLabel2[i].setVisible(true);
                }
            }
        }
    }

    public void makePawnVisible(int player, Controller controller, int index){
        if(player == 1){
            pawnLabel1[index].setVisible(true);
        }else{
            pawnLabel2[index].setVisible(true);
        }
    }

    public void movePawnFromToPosition(int player, int path, int prvPosition, int nextPosition){
        if(player == 1){
            if(path == 0){
                path1Labels[prvPosition-1].remove(pawnLabel1[path]);

                path1Labels[nextPosition-1].add(pawnLabel1[path]);
            }else if(path == 1){
                path2Labels[prvPosition-1].remove(pawnLabel1[path]);

                path2Labels[nextPosition-1].add(pawnLabel1[path]);
            }else if(path == 2){
                path3Labels[prvPosition-1].remove(pawnLabel1[path]);

                path3Labels[nextPosition-1].add(pawnLabel1[path]);
            }else{
                path4Labels[prvPosition-1].remove(pawnLabel1[path]);

                path4Labels[nextPosition-1].add(pawnLabel1[path]);
            }
        }else{
            if(path == 0){
                path1Labels[prvPosition-1].remove(pawnLabel2[path]);

                path1Labels[nextPosition-1].add(pawnLabel2[path]);
            }else if(path == 1){
                path2Labels[prvPosition-1].remove(pawnLabel2[path]);

                path2Labels[nextPosition-1].add(pawnLabel2[path]);
            }else if(path == 2){
                path3Labels[prvPosition-1].remove(pawnLabel2[path]);

                path3Labels[nextPosition-1].add(pawnLabel2[path]);
            }else{
                path4Labels[prvPosition-1].remove(pawnLabel2[path]);

                path4Labels[nextPosition-1].add(pawnLabel2[path]);
            }
        }
        path1Labels[prvPosition-1].revalidate();
        path1Labels[prvPosition-1].repaint();

        path1Labels[nextPosition-1].revalidate();
        path1Labels[nextPosition-1].repaint();

        path2Labels[prvPosition-1].revalidate();
        path2Labels[prvPosition-1].repaint();

        path2Labels[nextPosition-1].revalidate();
        path2Labels[nextPosition-1].repaint();

        path3Labels[prvPosition-1].revalidate();
        path3Labels[prvPosition-1].repaint();

        path3Labels[nextPosition-1].revalidate();
        path3Labels[nextPosition-1].repaint();

        path4Labels[prvPosition-1].revalidate();
        path4Labels[prvPosition-1].repaint();

        path4Labels[nextPosition-1].revalidate();
        path4Labels[nextPosition-1].repaint();
    }

    public void updateInfoLabel(Controller controller){
        infoLabel.setText("<html>Available Cards: " + controller.getAvailableCards() +
                "<br>Check Points: " + controller.getCheckPoints() +
                "<br>Turn: " + controller.getTurn().getName() + "</html>");
    }

    public void makeImageNotGray(int player, int rareFind, Controller controller){
        ImageIcon image;
        if(rareFind == 0) {
            image = new ImageIcon(controller.getRareFinds().get(0).getImagePath());
        }else if(rareFind == 1){
            image = new ImageIcon(controller.getRareFinds().get(1).getImagePath());
        }else if(rareFind == 2){
            image = new ImageIcon(controller.getRareFinds().get(2).getImagePath());
        }else{
            image = new ImageIcon(controller.getRareFinds().get(3).getImagePath());
        }
        Image img = image.getImage().getScaledInstance(60, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledimage = new ImageIcon(img);
        if(player == 1){
            rareFinds1[rareFind].setIcon(scaledimage);
        }else{
            rareFinds2[rareFind].setIcon(scaledimage);
        }

    }


    public void addCardListener(MouseListener listenForCardButton){
        for (int i = 0; i < player1Cards.length; i++) {
            player1Cards[i].setActionCommand("player1-" + i);
            player1Cards[i].addMouseListener(listenForCardButton);
        }
        for (int i = 0; i < player2Cards.length; i++) {
            player2Cards[i].setActionCommand("player2-" + i);
            player2Cards[i].addMouseListener(listenForCardButton);
        }
    }

    public void addCardDeckListener(ActionListener listenForCardDeckButton){
        cardDeckButton.addActionListener(listenForCardDeckButton);
    }

    public void addNewGameListener(ActionListener listenForNewGameButton){
        newGameMenuItem.addActionListener(listenForNewGameButton);
    }

    public void addExitGameListener(ActionListener listenForExitGameButton){
        exitGameMenuItem.addActionListener(listenForExitGameButton);
    }

    public void addSaveGameListener(ActionListener listenForSaveGameButton){
        saveGameMenuItem.addActionListener(listenForSaveGameButton);
    }

    public void addContinueSavedGameListener(ActionListener listenForContinueSavedGameButton){
        continueGameMenuItem.addActionListener(listenForContinueSavedGameButton);
    }

    public void addMyMuralsButtonListener(ActionListener listenForMyMuralsButton){
        myMuralsButton1.setActionCommand("1");
        myMuralsButton1.addActionListener(listenForMyMuralsButton);

        myMuralsButton2.setActionCommand("2");
        myMuralsButton2.addActionListener(listenForMyMuralsButton);
    }

    public void addCastlesLabelListener(MouseAdapter listenForCastlesLabel){
        path1Labels[8].setName("0");
        path1Labels[8].addMouseListener(listenForCastlesLabel);

        path2Labels[8].setName("1");
        path2Labels[8].addMouseListener(listenForCastlesLabel);

        path3Labels[8].setName("2");
        path3Labels[8].addMouseListener(listenForCastlesLabel);

        path4Labels[8].setName("3");
        path4Labels[8].addMouseListener(listenForCastlesLabel);
    }

    // Open a popup that contains the error message passed

    public void displayErrorMessage(String errorMessage){

        JOptionPane.showMessageDialog(this, errorMessage);

    }

    public String choosePawn() {
        ImageIcon archaeologistIcon = new ImageIcon(new ImageIcon("project_assets/images/pionia/arch.jpg")
                .getImage()
                .getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        ImageIcon theseusIcon = new ImageIcon(new ImageIcon("project_assets/images/pionia/theseus.jpg")
                .getImage()
                .getScaledInstance(100, 100, Image.SCALE_SMOOTH));

        JLabel archaeologistLabel = new JLabel("Archaeologist", archaeologistIcon, JLabel.CENTER);
        archaeologistLabel.setHorizontalTextPosition(JLabel.CENTER);
        archaeologistLabel.setVerticalTextPosition(JLabel.BOTTOM);

        JLabel theseusLabel = new JLabel("Theseus", theseusIcon, JLabel.CENTER);
        theseusLabel.setHorizontalTextPosition(JLabel.CENTER);
        theseusLabel.setVerticalTextPosition(JLabel.BOTTOM);

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(archaeologistLabel);
        panel.add(theseusLabel);

        int choice = JOptionPane.showOptionDialog(
                null,
                panel,
                "Choose a Pawn",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Archaeologist", "Theseus"},
                null
        );

        if (choice == 0) {
            return "arch";
        } else if (choice == 1) {
            return "theseus";
        }
        return null;
    }

    public int openBox() {
        while (true) {
            ImageIcon archaeologistIcon = new ImageIcon(new ImageIcon("project_assets/images/pionia/arch.jpg")
                    .getImage()
                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH));

            JLabel archaeologistLabel = new JLabel("Archaeologist", archaeologistIcon, JLabel.CENTER);
            archaeologistLabel.setHorizontalTextPosition(JLabel.CENTER);
            archaeologistLabel.setVerticalTextPosition(JLabel.BOTTOM);
            archaeologistLabel.setFont(new Font("Arial", Font.PLAIN, 13));

            JTextArea questionText = new JTextArea(
                    "Do you want to open the box? You will be visible to the other player. "
                            + "But you will add a finding to your collection."
            );
            questionText.setWrapStyleWord(true);
            questionText.setLineWrap(true);
            questionText.setOpaque(false);
            questionText.setEditable(false);
            questionText.setFocusable(false);
            questionText.setBackground(UIManager.getColor("Label.background"));
            questionText.setFont(new Font("Arial", Font.PLAIN, 16));

            JPanel panel = new JPanel(new BorderLayout(20, 20));
            panel.add(questionText, BorderLayout.NORTH);
            panel.add(archaeologistLabel, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(300, 200));

            String[] options = {"YES", "NO"};
            int choice = JOptionPane.showOptionDialog(null, panel, "Open the Box", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

            if (choice == JOptionPane.YES_OPTION) {
                return 1;
            } else if (choice == JOptionPane.NO_OPTION) {
                return 0;
            }
        }
    }


    public int destroyBox() {
        while (true) {
            ImageIcon theseusIcon = new ImageIcon(new ImageIcon("project_assets/images/pionia/theseus.jpg")
                    .getImage()
                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH));


            JLabel theseusLabel = new JLabel("Theseus", theseusIcon, JLabel.CENTER);
            theseusLabel.setHorizontalTextPosition(JLabel.CENTER);
            theseusLabel.setVerticalTextPosition(JLabel.BOTTOM);
            theseusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

            JTextArea questionText = new JTextArea(
                    "Do you want to destroy the finding in the box?"
                            + "You will be visible to the other player."
            );
            questionText.setWrapStyleWord(true);
            questionText.setLineWrap(true);
            questionText.setOpaque(false);
            questionText.setEditable(false);
            questionText.setFocusable(false);
            questionText.setBackground(UIManager.getColor("Label.background"));
            questionText.setFont(new Font("Arial", Font.PLAIN, 16));

            JPanel panel = new JPanel(new BorderLayout(20, 20));
            panel.add(questionText, BorderLayout.NORTH);
            panel.add(theseusLabel, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(300, 200));

            String[] options = {"YES", "NO"};
            int choice = JOptionPane.showOptionDialog(null, panel, "Destroy the Box", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

            if (choice == JOptionPane.YES_OPTION) {
                return 1;
            } else if (choice == JOptionPane.NO_OPTION) {
                return 0;
            }
        }
    }

    public int endGame(String playerText) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel winnerLabel = new JLabel(playerText , JLabel.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        if(playerText.equals("Player1 won!")){
            winnerLabel.setForeground(Color.RED);
        }else if(playerText.equals("Player2 won!")){
            winnerLabel.setForeground(Color.GREEN);
        }
        panel.add(winnerLabel, BorderLayout.CENTER);

        JLabel playAgainLabel = new JLabel("Do you want to play again?", JLabel.CENTER);
        playAgainLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(playAgainLabel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(200, 200));

        if(playerText.equals("Player1 won!")){
            panel.setBorder(new LineBorder(Color.RED, 5));
        }else if(playerText.equals("Player2 won!")){
            panel.setBorder(new LineBorder(Color.GREEN, 5));
        }else{
            panel.setBorder(new LineBorder(Color.BLACK, 5));
        }

        String[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(null, panel, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);


        if (choice == JOptionPane.YES_OPTION) {
            return 1;
        }else{
            this.dispose();
            System.exit(0);
        }
        return 0;

    }


}


