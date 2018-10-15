package CPLEXUtil.cplexutils;

/**
 * Custom exception class: reports a null/empty argument.
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public class NullArgumentException extends Exception {
  /**
   * Constructor.
   */
  public NullArgumentException() {
    super("Null or empty argument passed to set method.");
  }
}