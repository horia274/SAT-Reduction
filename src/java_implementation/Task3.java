
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;
    private int numberOfFamilies;
    private int numberOfRelations;
    private boolean[][] matrixOfRelations;

    private int maxDimensionOfClique;
    private boolean foundMaxClique;
    private boolean[] isInnocent;

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        /* start with the maximum possible clique */
        maxDimensionOfClique = numberOfFamilies + 1;
        while (!foundMaxClique) {
            /* continue until a clique is found */
            maxDimensionOfClique--;
            reduceToTask2();
            task2Solver.solve();
            extractAnswerFromTask2();
        }

        writeAnswer();
    }

    /**
     * read the problem input (inFilename) and
     * store the data in the object's attributes
     * @throws IOException checks input-output errors
     */
    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));

        numberOfFamilies = scanner.nextInt();
        numberOfRelations = scanner.nextInt();
        matrixOfRelations = new boolean[numberOfFamilies][numberOfFamilies];

        /* compute adjacent matrix of the given graph */
        for (int i = 0; i < numberOfRelations; i++) {
            int firstFamily = scanner.nextInt();
            int secondFamily = scanner.nextInt();

            matrixOfRelations[firstFamily - 1][secondFamily - 1] = true;
            matrixOfRelations[secondFamily - 1][firstFamily - 1] = true;
        }
    }

    /**
     * reduce the current problem to Task2
     * @throws FileNotFoundException checks input-output errors
     */
    public void reduceToTask2() throws FileNotFoundException {
        PrintStream printStream = new PrintStream(task2InFilename);

        /* create input for task2 with complementary graph */
        int numberOfReversedRelations = numberOfFamilies
                * (numberOfFamilies - 1) / 2 - numberOfRelations;

        printStream.println(numberOfFamilies + " " + numberOfReversedRelations
                + " " + maxDimensionOfClique);

        for (int i = 1; i < numberOfFamilies; i++) {
            for (int j = i + 1; j <= numberOfFamilies; j++) {
                if (!matrixOfRelations[i - 1][j - 1]) {
                    printStream.println(i + " " + j);
                }
            }
        }
    }

    /**
     * extract the current problem's answer from Task2's answer
     * @throws FileNotFoundException checks input-output errors
     */
    public void extractAnswerFromTask2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(task2OutFilename));
        String answer = scanner.nextLine();
        if (answer.equals("True")) {
            /* when get positive response, stop the algorithm */
            foundMaxClique = true;
            /* save the families from clique as innocent ones */
            isInnocent = new boolean[numberOfFamilies];
            for (int i = 0; i < maxDimensionOfClique; i++) {
                int currentInnocent = scanner.nextInt();
                isInnocent[currentInnocent - 1] = true;
            }
        }
    }

    /**
     * write the answer to the current problem (outFilename)
     * @throws IOException checks input-output errors
     */
    @Override
    public void writeAnswer() throws IOException {
        PrintStream printStream = new PrintStream(outFilename);
        for (int i = 0; i < numberOfFamilies; i++) {
            /* print guilty families */
            if (!isInnocent[i]) {
                printStream.print((i + 1) + " ");
            }
        }
    }
}
