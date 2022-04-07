import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * This program finds different ways one can travel by bus (with a bit 
 * of walking) from one bus stop to another.
 *
 * @author: Ooi Wei Tsang
 * @version: CS2030S AY21/22 Semester 2, Lab 8
 */
public class Main {
  /**
   * The program read a sequence of (id, search string) from either
   * standard input or a file.  If an invalid filename is given, the 
   * program would quit with an error message.
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    Instant start = Instant.now();
    try {
      ArrayList<CompletableFuture<Void>> allCFs = new ArrayList<>();
      ArrayList<String> descriptions = new ArrayList<>();

      Scanner sc = createScanner(args);
      while (sc.hasNext()) {
        BusStop srcId = new BusStop(sc.next());
        String searchString = sc.next();
        allCFs.add(BusSg
            .findBusServicesBetween(srcId, searchString)
            .thenCompose(busRoutes -> busRoutes.description())
            .thenAccept(description -> {
              descriptions.add(description);
            })
        );
      }
      sc.close();

      @SuppressWarnings("rawtypes")
      CompletableFuture<?>[] cfArray = (CompletableFuture<?>[]) allCFs.toArray(
          new CompletableFuture[allCFs.size()]
      );
      CompletableFuture.allOf(cfArray)
          .thenRun(() -> {
            descriptions.stream()
                .forEach(description -> System.out.println(description));
          })
          .join();
    } catch (FileNotFoundException exception) {
      System.err.println("Unable to open file " + args[0] + " "
          + exception);
    }
    Instant stop = Instant.now();
    System.out.printf("Took %,dms\n", Duration.between(start, stop).toMillis());
  }

  /**
   * Create and return a scanner. If a command line argument is given,
   * treat the argument as a file and open a scanner on the file. Else,
   * create a scanner that reads from standard input.
   *
   * @param args The arguments provided for simulation.
   * @return A scanner.
   * @throws FileNotFoundException Throws if filename given is not valid.
   */
  private static Scanner createScanner(String[] args) throws FileNotFoundException {
    // Read from stdin if no filename is given, otherwise read from the
    // given file.
    if (args.length == 0) {
      // If there is no argument, read from standard input.
      return new Scanner(System.in);
    } 
    // Else read from file
    FileReader fileReader = new FileReader(args[0]);
    return new Scanner(fileReader);
  }
}
