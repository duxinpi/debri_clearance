package CPLEXUtil.cplexutils;

import ilog.cp.IloCP;
import ilog.cplex.IloCplex;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * OptionsCatalog recursively parses a class, identifying its fields (and the
 * fields of its subclasses) by name and type.
 *
 * @author Paul A. Rubin (rubin@msu.edu)
 * @version 2.0.1
 */
public class OptionsCatalog {
  /**
   * Solver specifies one of the IBM solvers.
   */
  public static enum Solver { CPLEX, CPOPTIMIZER };
  private final HashMap<String, HashSet<Field>> field;
    // maps field name X.Y.Z.W to matching fields via all possible keys
    // (w, z.w, y.z.w, x.y.z.w), all keys in lower case
  private final HashMap<Field, Class> fieldType;
    // maps fields to types
  private int longestName;
    // length of the longest name
  private String rootName;
    // portion of the root class name to remove from all names
  private String baseName;
    // the common portion of all parameter names
  private final String solverName;  // the name of the solver

  /**
   * Constructor.
   * @param s the solver whose options should be cataloged.
   */
  public OptionsCatalog(final Solver s) {
    // create the data structures
    field = new HashMap<>();
    fieldType = new HashMap<>();
    // initialize fields
    longestName = 0;
    // proceed based on the selected solver
    switch (s) {
      case CPLEX:
        solverName = "CPLEX";
        rootName = "ilog.cplex.";
        baseName = "IloCplex.Param.";
        parse(IloCplex.Param.class);
        break;
      case CPOPTIMIZER:
        solverName = "CPOptimizer";
        rootName = "ilog.cp.";
        baseName = "IloCP.";
        parse(IloCP.IntParam.class);     // parse integer parameters
        parse(IloCP.DoubleParam.class);  // parse double paramemters
        break;
      default:
        solverName = null;
    }
  }

  /**
   * Recursively parse a class and all subclasses. Only public fields are
   * parsed, and parameters whose names begin with a leading underscore
   * are ignored.
   * @param c the class to parse
   */
  private void parse(final Class c) {
    // parse all fields of this class
    for (Field f : c.getFields()) {
      String n = f.getName();
      longestName = Math.max(longestName, n.length());
      if (!n.startsWith("_")) {
        // get the fully qualified name
        String fullName =
                f.getDeclaringClass().getCanonicalName() + "." + f.getName();
        // remove the parent class portion
        fullName = trimRootName(fullName);
        // split the name at each dot
        String[] chunk = fullName.split("\\.");
        // convert the chunks to lower case
        for (int i = 0; i < chunk.length; i++) {
          chunk[i] = chunk[i].toLowerCase();
        }
        // reverse the list
        HashSet<Field> fset;
        // map consecutively longer portions of the full name to the field
        String name = "";
        for (int i = chunk.length - 1; i >= 0; i--) {
          name = chunk[i] + ((name.isEmpty()) ? "" : ".") + name;
          if (field.containsKey(name)) {
            fset = field.get(name);
          } else {
            fset = new HashSet<>();
            field.put(name, fset);
          }
          fset.add(f);
        }
        // map the field to its type
        fieldType.put(f, fieldType(f));
      }
    }
    // parse all public subclasses recursively
    for (Class z : c.getClasses()) {
      parse(z);
    }
  }

  /**
   * Identify the type of a parameter field.
   * @param f the field
   * @return the class of that field (null if not recognized)
   */
  public final Class fieldType(final Field f) {
    // convert the field type to a string
    String t = f.getType().toString();
    // isolate the part after the dollar sign
    int k = t.lastIndexOf("$");
    if (k >= 0) {
      t = t.substring(k + 1);
    }
    switch (t) {
      case "BooleanParam":
        return boolean.class;
      case "DoubleParam":
        return double.class;
      case "IntParam":
        return int.class;
      case "LongParam":
        return long.class;
      case "StringParam":
        return String.class;
      default:
        return null;
    }
  }

  /**
   * Get the list of fully qualified parameter names.
   * @return the alphabetized list of fully qualified parameter names
   */
  public final List<String> fieldNames() {
    ArrayList<String> list = new ArrayList<>();
    for (Field f : fieldType.keySet()) {
      list.add(f.getDeclaringClass().getCanonicalName() + "." + f.getName());
    }
    Collections.sort(list);
    return list;
  }

  /**
   * Summarize the parameter fields found.
   * @return a string summarizing the fields
   */
  @Override
  public final String toString() {
    if (solverName == null) {
      return "";
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append(solverName)
        .append(":");
      // alphabetize the fields by field name
      ArrayList<Field> fields = new ArrayList<>(fieldType.keySet());
      Collections.sort(fields, new Comparator<Field>() {
        @Override
        public int compare(final Field o1, final Field o2) {
          return o1.getName().compareToIgnoreCase(o2.getName());
        }
      });
      for (Field f : fields) {
        // get the field type, changing "java.lang.String" to just "String"
        String type =
          (fieldType.get(f) == String.class) ? "String"
                                             : fieldType.get(f).getName();
        sb.append(String.format("\n\t%" + longestName + "s\t%s\t\t%s",
                                f.getName(),
                                type,
                                fullName(f)));
      }
      return sb.toString();
    }
  }

  /**
   * Trim the root portion of a class/field name from the name.
   * @param n the name to trim
   * @return the trimmed name
   */
  private String trimRootName(final String n) {
    if (n.startsWith(rootName)) {
      return n.substring(rootName.length());
    } else {
      return n;
    }
  }

  /**
   * Get the fully qualified name of a field.
   * @param f the field
   * @return the full name (less the initial root)
   */
  private String fullName(final Field f) {
    // get the name of the parent class
    String n = f.getDeclaringClass().getCanonicalName();
    // trim the initial root
    n = trimRootName(n);
    // append the field name
    n += "." + f.getName();
    return n;
  }

  /**
   * Get a list of fully qualified names of fields matching a given
   * partial name.
   * @param n the partial name
   * @return the list of matching full names
   */
  public final List<String> matches(final String n) {
    ArrayList<String> names = new ArrayList<>();
    if (field.containsKey(n.toLowerCase())) {
      for (Field f : field.get(n.toLowerCase())) {
        names.add(fullName(f));
      }
    }
    Collections.sort(names);
    return names;
  }

  /**
   * Get the parameter field matching a given name (completely or partially
   * specified and case-insensitive).
   * @param name the parameter name
   * @return the corresponding parameter field
   * @throws UnknownParameterException if the name does not match any parameter
   * @throws AmbiguousParameterException if the name matches multiple parameters
   */
  public final Field getField(final String name)
                     throws UnknownParameterException,
                            AmbiguousParameterException {
    HashSet<Field> choices = field.get(name.toLowerCase());
    if (choices == null) {
      throw new UnknownParameterException(solverName, name);
    } else if (choices.size() > 1) {
      // the parameter name is ambiguous UNLESS one of the choices is
      // the specified name prefixed with just the base name
      String n = baseName + name;
      for (Field f : choices) {
        if (trimRootName(fullName(f)).equalsIgnoreCase(n)) {
          return f;
        }
      }
      throw new AmbiguousParameterException(solverName, name, matches(name));
    } else {
      return choices.iterator().next();
    }
  }

}