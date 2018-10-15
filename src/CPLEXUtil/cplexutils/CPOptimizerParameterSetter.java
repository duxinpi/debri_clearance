package CPLEXUtil.cplexutils;

import ilog.concert.IloException;
import ilog.cp.IloCP;
import java.lang.reflect.Field;

/**
 * CPOptimizerParameterSetter provides a mechanism for specifying CP Optimizer
 * parameters by name in the command line of a Java program.
 *
 * <p>Example: Suppose that a program is called with</p>
 * <pre>
 *   java -jar myprog.jar TimeLimit 30 BranchLimit 12
 * </pre>
 * <p>Within the main program,</p>
 * <pre>
 *   IloCP c;
 *   CPOptimizerParameterSetter setter = new CPOptimizerParameterSetter();
 *   setter.set(c, args[0], args[1]);
 *   setter.set(c, args[2], args[3]);
 * </pre>
 * <p>(within a try-catch block) creates a CP Optimizer instance and sets the
 * time limit parameter to 30 seconds and the branch limit to 12.</p>
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 *
 * Licensed under the Eclipse Public License 1.0
 * (http://www.eclipse.org/legal/epl-v10.html).
 */
public class CPOptimizerParameterSetter {
  private final OptionsCatalog catalog;  // catalog of known options

  /**
   * Constructor.
   */
  public CPOptimizerParameterSetter() {
    catalog = new OptionsCatalog(OptionsCatalog.Solver.CPOPTIMIZER);
  }

  /**
   * Set the value of a CP Optimizer parameter.
   * @param cp the solver instance in which the parameter will be set
   * @param name the (case-sensitive) Java symbolic name of the parameter
   * @param value a string containing the new value for the parameter
   * @throws UnknownParameterException if the parameter name is not recognized
   * @throws NullArgumentException if a null or empty argument is passed
   * @throws IloException if CP Optimizer balks at the parameter value
   * @throws IllegalAccessException (should never happen)
   * @throws AmbiguousParameterException if the parameter name is ambiguous
   */
  public final void set(final IloCP cp, final String name, final String value)
                    throws UnknownParameterException,
                           NullArgumentException,
                           IloException,
                           IllegalAccessException,
                           AmbiguousParameterException {
    // check for invalid inputs
    if (cp == null || name == null || name.isEmpty()
        || value == null || value.isEmpty()) {
      throw new NullArgumentException();
    }
    // get the named field
    Field f = catalog.getField(name);
    Object param = f.get(cp);
    // get the type of the field
    Class t = catalog.fieldType(f);
    /*
      Unlike CPLEX, CP Optimizer only supports two types of parameters:
      double and integer.
    */
    // try to set the parameter
    if (t == int.class) {
      int v = Integer.parseInt(value);
      cp.setParameter((IloCP.IntParam) param, v);
    } else if (t == double.class) {
      double v = Double.parseDouble(value);
      cp.setParameter((IloCP.DoubleParam) param , v);
    }
  }
}
