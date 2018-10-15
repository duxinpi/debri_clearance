package CPLEXUtil.test.cplex;


import CPLEXUtil.cplexutils.AmbiguousParameterException;
import CPLEXUtil.cplexutils.CPOptimizerParameterSetter;
import CPLEXUtil.cplexutils.NullArgumentException;
import CPLEXUtil.cplexutils.UnknownParameterException;
import ilog.concert.IloException;
import ilog.cp.IloCP;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the CPOptimizerParameterSetter class.
 * @author Paul A. Rubin <parubin73@gmail.com>
 */
public class CPOptimizerParamSetterTest {
  IloCP cp;
  CPOptimizerParameterSetter setter;
  
  public CPOptimizerParamSetterTest() {
    cp = new IloCP();
    setter = new CPOptimizerParameterSetter();
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
    System.out.println("Testing with null arguments -- expecting exceptions.");
    System.out.println("Null value argument ...");
    try {
      setter.set(cp, "TimeLimit", null);
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
            AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Empty parameter argument ...");
    try {
      setter.set(cp, "", "234");
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
             AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    System.out.println("Null CP Optimizer object ...");
    try {
      IloCP c = null;
      setter.set(c, "TimeLimit", "30");
    } catch (NullArgumentException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch (UnknownParameterException | IloException |
             IllegalAccessException | NumberFormatException |
             AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
    // CPOptimizer has no ambiguous parameter names (as of CPLEX Studio
    // version 12.6), so nothing to test there.
  }
  
  /**
   * Try setting a nonexistent parameter.
   */
  @Test
  public void checkNoSuchParameter() {
    System.out.println("Testing with a nonexistent parameter --"
                       + " expecting an exception.");
    try {
      setter.set(cp, "TiLim", "30.0");
      // CPLEX has a "TiLim" parameter, but in CP Optimizer it is "TimeLimit"
    } catch(UnknownParameterException ex) {
      System.out.println("Found the expected exception: " + ex.getMessage());
    } catch(NullArgumentException | IloException | IllegalAccessException |
            NumberFormatException | AmbiguousParameterException ex) {
      fail("Unexpected Exception: " + ex.getMessage());
    }
  }

  /**
   * Test of set method, of class CPOptimizerParameterSetter.
   */
  @Test
  public void testSet() {
    System.out.println("Testing with valid arguments.");
    try {
      // try setting BranchLimit (integer parameter)
      String value = "20";
      setter.set(cp, "BranchLimit", value);
      assertEquals(cp.getParameter(IloCP.IntParam.BranchLimit), 20);
      System.out.println("Successfully set integer parameter (BranchLimit)");
      // try setting TimeLimit (double parameter)
      value = "999.9";
      setter.set(cp, "TimeLimit", value);
      assertEquals(cp.getParameter(IloCP.DoubleParam.TimeLimit), 999.9, .01);
      System.out.println("Successfully set double parameter (TimeLimit)");
    } catch (NullArgumentException | UnknownParameterException | IloException
             | IllegalAccessException | NumberFormatException
             | AmbiguousParameterException ex) {
      fail("Unexpected exception: " + ex);
    }
  }  
}