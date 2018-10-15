package CPLEXUtil.test.cplex;


import CPLEXUtil.cplexutils.AmbiguousParameterException;
import CPLEXUtil.cplexutils.CplexParameterSetter;
import CPLEXUtil.cplexutils.NullArgumentException;
import CPLEXUtil.cplexutils.UnknownParameterException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class CplexParameterSetterTest {
  IloCplex cpx;
  CplexParameterSetter setter;
  
  public CplexParameterSetterTest() {
    try {
      cpx = new IloCplex();
    } catch (IloException ex) {
      fail("Unable to initialize the CPLEX object!");
    }
    setter = new CplexParameterSetter();      
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  /**
   * Verify exception for null/empty arguments.
   */
  @Test
  public void checkExpectedException() {
    System.out.println("\nChecking expected exceptions ...");
    System.out.println("Testing with null arguments -- expecting exceptions.");
    System.out.println("Null value argument ...");
    try {
      setter.set(cpx, "Parallel", null);
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
            AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Empty parameter argument ...");
    try {
      setter.set(cpx, "", "234");
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
             AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Null CPLEX object ...");
    try {
      IloCplex c = null;
      setter.set(c, "MIP.Display", "30");
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
             AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Ambiguous parameter name ...");
    try {
      setter.set(cpx, "Display", "100");
    } catch (AmbiguousParameterException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
             NullArgumentException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Number format exception ...");
    try {
      setter.set(cpx, "MIP.Strategy.Search", "3.1415");      
    } catch (NumberFormatException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (AmbiguousParameterException | IllegalAccessException |
             IloException | NullArgumentException |
             UnknownParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
  }

  /**
   * Test of set method, of class CplexParameterSetter.
   */
  @Test
  public void testSet() {
    System.out.println("\nTesting with valid arguments ...");
    try {
      // try setting WorkDir (string parameter)
      String value = "nowhere";
      setter.set(cpx, "WorkDir", value);
      assertEquals(cpx.getParam(IloCplex.Param.WorkDir), value);
      System.out.println("Successfully set string parameter (WorkDir)");
      // try setting MIPSearch (integer parameter)
      value = "2";
      setter.set(cpx, "MIP.Strategy.Search", value);
      assertEquals(cpx.getParam(IloCplex.Param.MIP.Strategy.Search), 2);
      System.out.println("Successfully set integer parameter"
                         + " (MIP.Strategy.Search)");
      // repeat with a shorter name
      value = "1";
      setter.set(cpx, "Strategy.Search", value);
      assertEquals(cpx.getParam(IloCplex.Param.MIP.Strategy.Search), 1);
      System.out.println("Successfully set integer parameter (Strategy.Search)");
      // repeat with the shortest version of the name
      value = "0";
      setter.set(cpx, "Search", value);
      assertEquals(cpx.getParam(IloCplex.Param.MIP.Strategy.Search), 0);
      System.out.println("Successfully set integer parameter (Search)");
      // try setting UpperCutoff (double parameter)
      value = "999.9";
      setter.set(cpx, "UpperCutoff", value);
      assertEquals(cpx.getParam(IloCplex.Param.MIP.Tolerances.UpperCutoff),
                   999.9, .01);
      System.out.println("Successfully set double parameter (UpperCutoff)");
      // try setting MIP.Interval (long parameter)
      value = "2345";
      setter.set(cpx, "Interval", value);
      assertEquals(cpx.getParam(IloCplex.Param.MIP.Interval), 2345L);
      System.out.println("Successfully set long parameter (MIP.Interval)");
      // try setting Preprocesing.Presolve (boolean parameter);
      value = "FALSE";
      setter.set(cpx, "Presolve", value);
      assertEquals(cpx.getParam(IloCplex.BooleanParam.PreInd), false);
      System.out.println("Successfully set boolean parameter"
                         + " (Preprocesing.Presolve)");
      // verify that "TimeLimit", which lives at the base level of the
      // parameter scheme (IloCplex.Param.TimeLimit), works
      value = "12.35";
      setter.set(cpx, "TimeLimit", value);
      assertEquals(cpx.getParam(IloCplex.DoubleParam.TimeLimit), 12.35, .01);
      System.out.println("Successfully set TimeLimit without ambiguity.");
    } catch (NullArgumentException | UnknownParameterException | IloException
             | IllegalAccessException | NumberFormatException
             | AmbiguousParameterException ex) {
      fail("Unexpected exception: " + ex);
    }
  }
}