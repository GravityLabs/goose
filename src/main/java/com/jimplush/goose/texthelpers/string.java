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
package com.jimplush.goose.texthelpers;
/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/13/11
 * Time: 12:11 AM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class string {
  
  private string(){}

  public static final String empty = "";
  public static final String[] emptyArray = new String[] {empty};

  public static boolean isNullOrEmpty(String input) {
    if (input == null) return true;
    if (input.length() == 0) return true;
    return false;
  }

  public static StringSplitter SPACE_SPLITTER = new StringSplitter(" ");
}


