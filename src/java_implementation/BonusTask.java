
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Bonus Task
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class BonusTask extends Task {
    // TODO: define necessary variables and/or data structures
    private int numberOfFamilies;
    private int numberOfRelations;
    private boolean[][] matrixOfRelations;

    private int numberOfVariables;
    private int numberOfClauses;
    private int sumOfWeights;
    private List<List<Integer>> clauses;

    private int[] prisoners;

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
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

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle
        numberOfVariables = numberOfFamilies;
        sumOfWeights = numberOfFamilies;
        clauses = new ArrayList<>();

        for (int i = 1; i < numberOfFamilies; i++) {
            for (int j = i + 1; j <= numberOfFamilies; j++) {
                if (matrixOfRelations[i - 1][ j - 1]) {
                    List<Integer> clause = new ArrayList<>();
                    clause.add(sumOfWeights + 1);
                    clause.add(i);
                    clause.add(j);
                    clause.add(0);
                    clauses.add(clause);
                }
            }
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            List<Integer> clause = new ArrayList<>();
            clause.add(1);
            clause.add(-i);
            clause.add(0);
            clauses.add(clause);
        }

        numberOfClauses = clauses.size();

        PrintStream printStream = new PrintStream(oracleInFilename);
        printStream.println("p wcnf " + numberOfVariables + " " + numberOfClauses + " " + (sumOfWeights + 1));
        for (List<Integer> clause : clauses) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int variable : clause) {
                stringBuilder.append(variable).append(" ");
            }
            printStream.println(stringBuilder.toString());
        }
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        int counter = 0;

        numberOfVariables = scanner.nextInt();
        prisoners = new int[scanner.nextInt()];
        for (int i = 0; i < numberOfVariables; i++) {
            int currentVariable = scanner.nextInt();
            if (currentVariable > 0) {
                prisoners[counter++] = currentVariable;
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)
        PrintStream printStream = new PrintStream(outFilename);

        for (int prisoner : prisoners) {
            printStream.print(prisoner + " ");
        }
    }
}
