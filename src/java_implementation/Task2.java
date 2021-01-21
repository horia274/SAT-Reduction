
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Task2z
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {
    // TODO: define necessary variables and/or data structures
    private int numberOfFamilies;
    private int numberOfRelations;
    private int dimensionOfClique;
    private boolean[][] matrixOfRelations;

    private int numberOfVariables;
    private int numberOfClauses;
    private List<List<Integer>> clauses;

    private String oracleAnswer;
    private int[] familiesFromClique;

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
        dimensionOfClique = scanner.nextInt();
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
        numberOfVariables = numberOfFamilies * dimensionOfClique;
        clauses = new ArrayList<>();

        for (int i = 1; i <= dimensionOfClique; i++) {
            List<Integer> clause = new ArrayList<>();
            for (int j = 1; j <= numberOfFamilies; j++) {
                int FjPi = dimensionOfClique * (j - 1) + i;
                clause.add(FjPi);
            }
            clause.add(0);
            clauses.add(clause);
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j < dimensionOfClique; j++) {
                for (int k = j + 1; k <= dimensionOfClique; k++) {
                    List<Integer> clause = new ArrayList<>();
                    int FiPj = dimensionOfClique * (i - 1) + j;
                    int FiPk = dimensionOfClique * (i - 1) + k;
                    clause.add(-FiPj);
                    clause.add(-FiPk);
                    clause.add(0);
                    clauses.add(clause);
                }
            }
        }

        for (int i = 1; i < numberOfFamilies; i++) {
            for (int j = i + 1; j <= numberOfFamilies; j++) {
                if (!matrixOfRelations[i - 1][j - 1]) {
                    for (int k = 1; k <= dimensionOfClique; k++) {
                        for (int l = 1; l <= dimensionOfClique; l++) {
                            List<Integer> clause = new ArrayList<>();
                            int FiPk = dimensionOfClique * (i - 1) + k;
                            int FjPl = dimensionOfClique * (j - 1) + l;
                            clause.add(-FiPk);
                            clause.add(-FjPl);
                            clause.add(0);
                            clauses.add(clause);
                        }
                    }
                }
            }
        }

        numberOfClauses = clauses.size();

        PrintStream printStream = new PrintStream(oracleInFilename);
        printStream.println("p cnf " + numberOfVariables + " " + numberOfClauses);
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

        oracleAnswer = scanner.nextLine();
        if (oracleAnswer.equals("True")) {
            familiesFromClique = new int[dimensionOfClique];
            numberOfVariables = scanner.nextInt();
            int counter = 0;
            for (int i = 1; i <= numberOfVariables; i++) {
                int currentVariable = scanner.nextInt();
                if (currentVariable > 0) {
                    int family;
                    if (currentVariable % dimensionOfClique == 0) {
                        family = currentVariable / dimensionOfClique;
                    } else {
                        family = currentVariable / dimensionOfClique + 1;
                    }
                    familiesFromClique[counter++] = family;
                }
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)
        PrintStream printStream = new PrintStream(outFilename);

        printStream.println(oracleAnswer);
        if (oracleAnswer.equals("True")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < dimensionOfClique; i++) {
                stringBuilder.append(familiesFromClique[i]).append(" ");
            }
            printStream.println(stringBuilder.toString());
        }
    }
}
