package com.jimplush.goose; /**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/19/11
 * Time: 1:08 AM
 */

import junit.framework.*;

/**
 * This is not really a test nor is it a test suite. It is only meant to run all of the tests in
 * {@link GoldSitesTest} and print out a report of all of the tags collected durring those tests
 */
public class GoldenSuite extends TestCase {
  public void testRunSuite() {
    TestSuite suite = new TestSuite(GoldSitesTest.class);
    TestResult result = new TestResult();
    suite.run(result);
    GoldSitesTest.printReport();
  }

}


