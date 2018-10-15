package CPLEXUtil.cplexutils;

import ilog.concert.IloAddable;
import ilog.concert.IloConstraint;
import ilog.concert.IloObjective;
import ilog.cp.IloCP;
import java.util.Iterator;

/**
 * CPDisplay provides a static method (asString) that converts a constraint
 * program (instance of IloCP) to a listing (string) for display or printing.
 * Note: The listing will be <em>much</em> more readable if variables and
 * constraints are given descriptive names, either in their constructors (for
 * variables) or via the IloAddable.setName() method (for both variables and
 * constraints).
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public final class CPDisplay {
  /**
   * Constructor (does nothing -- the class should not be constructed).
   */
  private CPDisplay() { }

  /**
   * Convert an instance of IloCP to a string suitable for printing.
   * @param model the model instance to convert
   * @return a string representation of the model
   */
  public static String asString(final IloCP model) {
    if (model == null) {
      return "No model yet!";
    } else {
      StringBuilder sb = new StringBuilder();
      Iterator it = model.iterator();
      while (it.hasNext()) {                           // iterate over the model
        IloAddable object = (IloAddable) it.next();    // get the next object
        if (object instanceof IloObjective) {
          // For an objective, .toString() returns the body of the objective.
          sb.append(object.getName())
            .append(": ")
            .append(object)
            .append("\n");
        } else if (object instanceof IloConstraint) {
          /*
            For some constraints, .toString() returns the body; for others
            (notably inequalities), .toString() returns the name if there is
            one, the body if the name is null.
          */
          IloConstraint ct = (IloConstraint) object;
          String name = ct.getName();             // get the name
          ct.setName(null);                       // temporarily remove the name
          sb.append(name)
            .append(": ")
            .append(ct)                           // this now gets the body
            .append("\n");
          ct.setName(name);                       // restore the name
        } else {
          sb.append(object.getClass().toString()).append("\n");
        }
      }
      return sb.toString();
    }
  }
}
