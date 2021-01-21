
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {
    // TODO: define necessary variables and/or data structures
    private int numberOfFamilies;
    private int numberOfRelations;
    private int numberOfSpies;
    private int[][] relations;

    private int numberOfVariables;

    private String oracleAnswer;
    private int[] spiesPerFamily;

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
        numberOfSpies = scanner.nextInt();
        relations = new int[numberOfRelations][2];

        for (int i = 0; i < numberOfRelations; i++) {
            relations[i][0] = scanner.nextInt();
            relations[i][1] = scanner.nextInt();
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle
        numberOfVariables = numberOfFamilies * numberOfSpies;
        List<List<Integer>> clauses = new ArrayList<>();

        for (int i = 1; i <= numberOfFamilies; i++) {
            List<Integer> clause = new ArrayList<>();
            for (int j = 1; j <= numberOfSpies; j++) {
                int FiSj = numberOfSpies * (i - 1) + j;
                clause.add(FiSj);
            }
            clause.add(0);
            clauses.add(clause);
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j < numberOfSpies; j++) {
                for (int k = j + 1; k <= numberOfSpies; k++) {
                    List<Integer> clause = new ArrayList<>();
                    int FiSj = numberOfSpies * (i - 1) + j;
                    int FiSk = numberOfSpies * (i - 1) + k;
                    clause.add(-FiSj);
                    clause.add(-FiSk);
                    clause.add(0);
                    clauses.add(clause);
                }
            }
        }

        for (int m = 0; m < numberOfRelations; m++) {
            int i = relations[m][0];
            int j = relations[m][1];
            for (int k = 1; k <= numberOfSpies; k++) {
                List<Integer> clause = new ArrayList<>();
                int FiSk = numberOfSpies * (i - 1) + k;
                int FjSk = numberOfSpies * (j - 1) + k;
                clause.add(-FiSk);
                clause.add(-FjSk);
                clause.add(0);
                clauses.add(clause);
            }
        }

        int numberOfClauses = clauses.size();

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
            spiesPerFamily = new int[numberOfFamilies];
            numberOfVariables = scanner.nextInt();
            for (int i = 1; i <= numberOfVariables; i++) {
                int currentVariable = scanner.nextInt();
                if (currentVariable > 0) {
                    int family, spy;
                    if (currentVariable % numberOfSpies == 0) {
                        family = currentVariable / numberOfSpies;
                        spy = numberOfSpies;
                    } else {
                        family = currentVariable / numberOfSpies + 1;
                        spy = currentVariable % numberOfSpies;
                    }
                    spiesPerFamily[family - 1] = spy;
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
            for (int i = 0; i < numberOfFamilies; i++) {
                stringBuilder.append(spiesPerFamily[i]).append(" ");
            }
            printStream.println(stringBuilder.toString());
        }
    }
}
