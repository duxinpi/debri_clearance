package CPLEXUtil.cplexutils;

/**
 * Custom exception class: reports an unrecognized parameter name.
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public class UnknownParameterException extends Exception {

  /**
   * Constructor.
   * @param solver which solver (CPLEX or CP Optimizer) is involved
   * @param field the name of the alleged parameter
   */
    public UnknownParameterException(final String solver, final String field) {
      super("Cannot find a " + solver + " parameter named '" + field + "'.");
    }

}