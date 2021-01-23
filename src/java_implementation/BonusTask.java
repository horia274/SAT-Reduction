
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
    private int numberOfFamilies;
    private int numberOfRelations;
    private int[][] relations;

    private int numberOfVariables;

    private int[] prisoners;

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    /**
     * read the problem input (inFilename) and store the data in the object's attributes
     * @throws IOException checks input-output errors
     */
    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));

        numberOfFamilies = scanner.nextInt();
        numberOfRelations = scanner.nextInt();
        relations = new int[numberOfRelations][2];

        /* keep relation between families in array */
        for (int i = 0; i < numberOfRelations; i++) {
            relations[i][0] = scanner.nextInt();
            relations[i][1] = scanner.nextInt();
        }
    }

    /**
     * transform the current problem into a SAT problem and write it
     * (oracleInFilename) in a format understood by the oracle
     * @throws IOException checks input-output errors
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        PrintStream printStream = new PrintStream(oracleInFilename);

        /* compute number of variables */
        numberOfVariables = numberOfFamilies;
        /* compute number of clauses */
        int numberOfClauses = numberOfRelations + numberOfFamilies;
        /* compute sum of assigned weights */
        int sumOfWeights = numberOfFamilies;

        printStream.println("p wcnf " + numberOfVariables + " "
                + numberOfClauses + " " + (sumOfWeights + 1));

        /* clause type 1 - hard one */
        for (int k = 0; k < numberOfRelations; k++) {
            int Fi = relations[k][0];
            int Fj = relations[k][1];
            printStream.println((sumOfWeights + 1) + " " + Fi + " " + Fj + " " + 0);
        }

        /* clause type 2 - soft one */
        for (int i = 1; i <= numberOfFamilies; i++) {
            printStream.println(1 + " " + -i + " " + 0);
        }
    }

    /**
     * extract the current problem's answer from the answer
     * given by the oracle (oracleOutFilename)
     * @throws IOException checks input-output errors
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        int counter = 0;

        numberOfVariables = scanner.nextInt();
        prisoners = new int[scanner.nextInt()];
        for (int i = 0; i < numberOfVariables; i++) {
            int currentVariable = scanner.nextInt();
            if (currentVariable > 0) {
                /* if current variable is positive, arrest it */
                prisoners[counter++] = currentVariable;
            }
        }
    }

    /**
     * write the answer to the current problem (outFilename)
     * @throws IOException input-output errors
     */
    @Override
    public void writeAnswer() throws IOException {
        PrintStream printStream = new PrintStream(outFilename);

        /* print prisoners */
        for (int prisoner : prisoners) {
            printStream.print(prisoner + " ");
        }
    }
}
