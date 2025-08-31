package mvc.model.path;

import mvc.model.findings.Findings;

import java.io.Serializable;
import java.util.List;

import static mvc.model.path.PathColor.*;

/**
 * this class represents the board of the game, which consists of 4 paths
 */
public class Board implements Serializable {
    private final Path knossos;
    private final Path malia;
    private final Path phaistos;
    private final Path zakros;

    /**
     * constructor that creates the board
     * 4 paths that each have 9 positions
     */
    public Board() {
        knossos = new Path("Knossos", RED);
        malia = new Path("Malia", YELLOW);
        phaistos = new Path("Phaistos", GRAY);
        zakros = new Path("Zakros", BLUE);
    }

    /**
     * Accessor for the path knossos
     * @return the path knossos
     */
    public Path getKnossos() {
        return knossos;
    }

    /**
     * Accessor for the path malia
     * @return the path malia
     */
    public Path getMalia() {
        return malia;
    }

    /**
     * Accessor for the path phaistos
     * @return the path phaistos
     */
    public Path getPhaistos() {
        return phaistos;
    }

    /**
     * Accessor for the path zakros
     * @return the path zakros
     */
    public Path getZakros() {
        return zakros;
    }


    public void initializeBoard(List<Findings> findingsList) {
        knossos.initializePath(findingsList);
        malia.initializePath(findingsList);
        phaistos.initializePath(findingsList);
        zakros.initializePath(findingsList);
    }

}
