/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jimplush.goose; /**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/19/11
 * Time: 1:08 AM
 */

import junit.framework.*;

/**
 * This is not really a test nor is it a test suite. It is only meant to run all of the tests in
 * {@link GoldSitesTestIT} and print out a report of all of the tags collected durring those tests
 */
public class GoldenSuite extends TestCase {
  public void testRunSuite() {
    TestSuite suite = new TestSuite(GoldSitesTestIT.class);
    TestResult result = new TestResult();
    suite.run(result);
    GoldSitesTestIT.printReport();
  }

}


