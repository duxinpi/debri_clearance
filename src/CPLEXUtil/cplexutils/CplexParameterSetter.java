package CPLEXUtil.cplexutils;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.BooleanParam;
import ilog.cplex.IloCplex.DoubleParam;
import ilog.cplex.IloCplex.IntParam;
import ilog.cplex.IloCplex.LongParam;
import ilog.cplex.IloCplex.StringParam;
import java.lang.reflect.Field;

/**
 * CplexParameterSetter provides a mechanism for specifying CPLEX parameters
 * by name in the command line of a Java program.
 *
 * <p>Example: Suppose that a program is called with</p>
 * <pre>
 *    java -jar myprog.jar TimeLimit 30 MIP.Display 100
 * </pre><p>Within the main program,</p>
 * <pre>
 *   IloCplex c;
 *   CplexParameterSetter setter = new CplexParameterSetter();
 *   setter.set(c, args[0], args[1]);
 *   setter.set(c, args[2], args[3]);
 * </pre><p>(within a try-catch block) creates a CPLEX instance and sets
 * the time limit parameter to 30 seconds and the MIP log display interval
 * to 100.</p>
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 *
 * Licensed under the Eclipse Public License 1.0
 * (http://www.eclipse.org/legal/epl-v10.html).
 */
public class CplexParameterSetter {
  private final OptionsCatalog catalog;  // catalog of known options

  /**
   * Constructor.
   */
  public CplexParameterSetter() {
    catalog = new OptionsCatalog(OptionsCatalog.Solver.CPLEX);
  }

  /**
   * Set the value of a CPLEX parameter.
   * @param cplex the solver instance in which the parameter will be set
   * @param name the (case-sensitive) Java symbolic name of the parameter
   * @param value a string containing the new value for the parameter
   * @throws UnknownParameterException if the parameter name is not recognized
   * @throws NullArgumentException if a null or empty argument is passed
   * @throws IloException if CPLEX balks at the parameter value
   * @throws IllegalAccessException (should never happen)
   * @throws AmbiguousParameterException if the parameter name is ambiguous
   */
  public final void set(final IloCplex cplex,
                        final String name,
                        final String value)
                    throws UnknownParameterException,
                           NullArgumentException,
                           IloException,
                           IllegalAccessException,
                           AmbiguousParameterException {
    // check for invalid inputs
    if (cplex == null || name == null || name.isEmpty()
        || value == null || value.isEmpty()) {
      throw new NullArgumentException();
    }
    // get the named field
    Field f = catalog.getField(name);
    Object param = f.get(cplex);
    // get the type of the field
    Class t = catalog.fieldType(f);
    // try to set the parameter
    if (t == int.class) {
      int v = Integer.parseInt(value);
      cplex.setParam((IntParam) param, v);
    } else if (t == long.class) {
      long v = Long.parseLong(value);
      cplex.setParam((LongParam) param, v);
    } else if (t == double.class) {
      double v = Double.parseDouble(value);
      cplex.setParam((DoubleParam) param , v);
    } else if (t == boolean.class) {
      boolean v = Boolean.parseBoolean(value);
      cplex.setParam((BooleanParam) param, v);
    } else if (t == String.class) {
      cplex.setParam((StringParam) param, value);
    }
  }
}