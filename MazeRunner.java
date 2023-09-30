package maze_runner;

import maze_runner.generator.GraphBasedMazeGenerator;
import maze_runner.generator.OptimizedMazeGenerator;
import maze_runner.generator.SimpleImperfectMazeGenerator;
import maze_runner.generator.SimplePerfectMazeGenerator;

public class MazeRunner {

    public static final byte MAZE_SIMPLE = 0;
    public static final byte MAZE_GRAPH = 1;
    public static final byte MAZE_OPTIMIZED = 2;

    public static void main(String[] args) {
        if (args != null && args.length == 4) {
            try {
                Maze maze = createLabyrinth(args[0], args[1], args[2], args[3]);
                if (maze == null)
                    throw new NullPointerException("Le labyrinthe est null");

                printMazeTiles(maze);
            } catch (Exception e) {
                System.out.println("Erreur inattendue lors de la génération du labyrinthe. Veuillez réessayer.\nMessage: "+e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Erreur: Veuillez fournir les arguments nécessaires pour la génération du labyrinthe.");
            printHelpAndExit();
        }
    }

    private static Maze createLabyrinth(String widthStr, String heightStr, String mazeType, String generationMethod) {
        int width = 0;
        int height = 0;
        boolean usePerfectType = false;
        byte generationType = 0;

        // convert width and height args to integers
        try {
            width = Integer.parseInt(widthStr);
            height = Integer.parseInt(heightStr);
            if (width < 5 || height < 5) {
                System.out.println("Erreur : Veuillez fournir une largeur et une hauteur valides supérieurs ou égales à 5.");
                printHelpAndExit();
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Veuillez fournir une largeur et une hauteur valides supérieurs ou égales à 5.");
            printHelpAndExit();
        };

        // check maze type arg
        if (mazeType.equals("perfect")) {
            usePerfectType = true;
        } else if (!mazeType.equals("imperfect")) {
            System.out.println("Erreur : Veuillez fournir un type de labyrinthe et une méthode de génération valides.");
            printHelpAndExit();
        }

        // check generation method arg
        switch(generationMethod) {
            case "simple":
                generationType = MAZE_SIMPLE;
                break;
            case "graph":
                generationType = MAZE_GRAPH;
                break;
            case "optimized":
                generationType = MAZE_OPTIMIZED;
                break;
            default:
                System.out.println("Erreur : Veuillez fournir un type de labyrinthe et une méthode de génération valides.");
                printHelpAndExit();
        }

        Maze maze = null;
        switch(generationType) {
            case MAZE_SIMPLE:
                if (!usePerfectType) {
                    maze = new SimpleImperfectMazeGenerator().generate(width, height);
                } else {
                    maze = new SimplePerfectMazeGenerator().generate(width, height);
                }
                break;
            case MAZE_GRAPH:
                maze = new GraphBasedMazeGenerator(usePerfectType).generate(width, height);
                break;
            case MAZE_OPTIMIZED:
                maze = new OptimizedMazeGenerator(usePerfectType).generate(width, height);
                break;
        }

        return maze;
    }

    public static void printMazeTiles(Maze maze) {
        for (int y = 0; y < maze.getHeight(); y++) {

            // print 3 lines for 1 tile
            for (int i = 0; i < 3; i++) {

                for (int x = 0; x < maze.getWidth(); x++) {
                    byte tileType = maze.getTile(x, y).getTileType();

                    System.out.print(MazeTile.MAZE_TILE_PATTERNS[tileType].substring(i*3, (i*3)+3));
                }
                System.out.println();
            }
        }
    }

    private static void printHelpAndExit() {
        System.out.println("Utilisation : java -jar MazeRunner.jar [largeur] [hauteur] [perfect/imperfect] [simple/graph/optimized]");
        System.exit(1);
    }

}