package mvc.model.path;

import mvc.model.findings.Findings;
import mvc.model.findings.RareFinds;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * this class represents the path of the game
 */
public class Path implements Serializable {
    private final String name;
    private final int lenght;
    private final Position[] positions;
    private final PathColor color;

    /**
     * constructor that creates a path that has 9 positions
     * @param name of the path
     * @param color of the path
     */
    public Path(String name, PathColor color) {
        this.name = name;
        this.lenght = 9;
        this.positions = new Position[9];
        this.color = color;
    }

    /**
     * method getName : (Accessor) access the path's name
     * @return the name of the path
     */
    public String getName(){
        return this.name;
    }

    /**
     * method getPosition : (Accessor) to access the position in the path
     * @param number the number (the index) of the path starting from 1
     * @return the Position in the path that we are looking for
     */
    public Position getPosition(int number){
        return this.positions[number-1];
    }

    /**
     * method getColor : (Accessor) to access the color of the path
     *
     * @return the color of the path
     */
    public Color getColor(){
        return this.color.getAwtColor();
    }

    /**
     * method initializePath : initializes the path with 9 positions and
     * give each position a number from 1 to 9
     * give each position its points from -20 until 50
     * and give each position the same color as the path's color
     * and put random findings in positions 2, 4, 6, 8, 9 with its corresponding image
     * (2 4 6 8) have different images than (1 3 5 7) and the (9) is different
     * (if it has a finding use an image, and if it doesn't have a finding use different image)
     * by first putting a Rare find and then the other finds randomly
     * (also the number 7 is a checkpoint) this is just info we dont change anything in the implementation
     * @param findingsList that are left to be added to the path
     * @post the path is initialized with 9 positions that each has a different number, points, findings...
     */
    public void initializePath(List<Findings> findingsList){
        int[] points = {-20, -15, -10, 5, 10, 15, 30, 35, 50};
        String name = "project_assets/images/paths/" + this.getName().toLowerCase();
        String[] imagePaths = {name + ".jpg",name + "2.jpg",name + "Palace.jpg"};

        Findings[] findings = new Findings[5];
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        int i = 0;
        for(; i < findingsList.size(); i++) {
            if (findingsList.get(i) instanceof RareFinds){
                if(Objects.equals(((RareFinds) findingsList.get(i)).getPath().getName(), this.getName())){break;}
            }
        }
        findings[randomNumber] = findingsList.get(i);
        findingsList.remove(i);
        for(int k = 0; k < 5; k++){
            if(k != randomNumber){
                int index = 0;
                while(index < findingsList.size()){
                    if(findingsList.get(index) != null){
                        if(!(findingsList.get(index) instanceof RareFinds)){
                            findings[k] = findingsList.get(index);
                            findingsList.remove(index);
                            break;
                        }
                    }
                    index++;
                }
            }
        }
        int findIndex = 0;
        for(int j = 0; j < 9; j++){
            if(j % 2 == 0 && j != 8) {
                positions[j] = new Position(j+1, points[j], null, color, imagePaths[0]);
            }else if(j % 2 == 1) {
                positions[j] = new Position(j+1, points[j], findings[findIndex], color, imagePaths[1]);
                findIndex++;
            }
            else if(j == 8){
                positions[j] = new Position(j+1, points[j], findings[findIndex], color, imagePaths[2]);
            }
        }

    }
}
