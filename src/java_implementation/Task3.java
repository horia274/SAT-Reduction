
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
    // TODO: define necessary variables and/or data structures
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

        // TODO: implement a way of successively querying the oracle (using Task2) about various arrest numbers until you
        //  find the minimum

        maxDimensionOfClique = numberOfFamilies + 1;
        while (!foundMaxClique) {
            maxDimensionOfClique--;
            reduceToTask2();
            task2Solver.solve();
            extractAnswerFromTask2();
        }

        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes
        Scanner scanner = new Scanner(new File(inFilename));

        numberOfFamilies = scanner.nextInt();
        numberOfRelations = scanner.nextInt();
        matrixOfRelations = new boolean[numberOfFamilies][numberOfFamilies];

        for (int i = 0; i < numberOfRelations; i++) {
            int firstFamily = scanner.nextInt();
            int secondFamily = scanner.nextInt();

            matrixOfRelations[firstFamily - 1][secondFamily - 1] = true;
            matrixOfRelations[secondFamily - 1][firstFamily - 1] = true;
        }
    }

    public void reduceToTask2() throws FileNotFoundException {
        // TODO: reduce the current problem to Task2
        PrintStream printStream = new PrintStream(task2InFilename);
        int numberOfReversedRelations = numberOfFamilies * (numberOfFamilies - 1) / 2 - numberOfRelations;
        printStream.println(numberOfFamilies + " " + numberOfReversedRelations + " " + maxDimensionOfClique);
        for (int i = 1; i < numberOfFamilies; i++) {
            for (int j = i + 1; j <= numberOfFamilies; j++) {
                if (!matrixOfRelations[i - 1][j - 1]) {
                    printStream.println(i + " " + j);
                }
            }
        }
    }

    public void extractAnswerFromTask2() throws FileNotFoundException {
        // TODO: extract the current problem's answer from Task2's answer
        Scanner scanner = new Scanner(new File(task2OutFilename));
        String answer = scanner.nextLine();
        if (answer.equals("True")) {
            foundMaxClique = true;
            isInnocent = new boolean[numberOfFamilies];
            for (int i = 0; i < maxDimensionOfClique; i++) {
                int currentInnocent = scanner.nextInt();
                isInnocent[currentInnocent - 1] = true;
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)
        PrintStream printStream = new PrintStream(outFilename);
        for (int i = 0; i < numberOfFamilies; i++) {
            if (!isInnocent[i]) {
                printStream.print(i + 1 + " ");
            }
        }
    }
}
