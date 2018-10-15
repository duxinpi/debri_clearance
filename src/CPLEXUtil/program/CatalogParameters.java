package CPLEXUtil.program;


import CPLEXUtil.cplexutils.OptionsCatalog;

/**
 * CatalogParameters lists all parameters known to CPLEX and to CPOptimizer.
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public final class CatalogParameters {

  /**
   * Constructor.
   */
  private CatalogParameters() { }

  /**
   * List the parameter catalogs.
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    // parse CPLEX parameters
    OptionsCatalog cplex = new OptionsCatalog(OptionsCatalog.Solver.CPLEX);
    System.out.println(cplex);
    // parse CPOptimizer parameters
    OptionsCatalog cp = new OptionsCatalog(OptionsCatalog.Solver.CPOPTIMIZER);
    System.out.println("\n" + cp);
  }

}