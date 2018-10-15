package CPLEXUtil.cplexutils;

import java.util.List;

/**
 * Custom exception class: reports an ambiguously specified parameter.
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public class AmbiguousParameterException extends Exception {

  /**
   * Constructor.
   * @param s the name of the solver
   * @param n the ambiguous name for the parameter
   * @param matches a list of matching full names
   */
  public AmbiguousParameterException(final String s,
                                     final String n,
                                     final List<String> matches) {
    super(makeMessage(s, n, matches));
  }

  /**
   * Utility method to create a message for an ambiguous parameter exception.
   * @param s the name of the solver
   * @param n the ambiguous name for the parameter
   * @param matches a list of matching full names
   * @return a message to include in the exception
   */
  private static String makeMessage(final String s,
                                    final String n,
                                    final List<String> matches) {
    StringBuilder sb = new StringBuilder();
    sb.append(s)
      .append(" parameter name '")
      .append(n)
      .append("' matches all the following:");
    for (String m : matches) {
      sb.append("\n\t")
        .append(m);
    }
    return sb.toString();
  }

}