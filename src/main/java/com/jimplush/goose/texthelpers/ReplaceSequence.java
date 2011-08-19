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
 * Time: 12:03 AM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps the usage of making multiple string replacements in an ordered sequence.
 * For Example... instead of doing this over and over:</p>
 * <blockquote>
 *   <pre>
 *     String text = "   Some example text     ";
 *     text = text.{@link String#replaceAll(String, String) replaceAll}("e", "E");
 *     text = text.{@link String#replaceAll(String, String) replaceAll}(" ", "_");
 *     text = text.{@link String#replaceAll(String, String) replaceAll}("^\\s+$", "");
 *   </pre>
 * </blockquote>
 * You can use a <code>ReplaceSequence</code> like this:</p>
 * <blockquote>
 *   <pre>
 *     static final betterReplacements = ReplaceSequence.{@link #create(String, String) create}("e", "E").{@link #append(String, String) append}(" ", "_").{@link #append(String) append}("^\\s+$");
 *
 *     void fixMyString(String text) {
 *       return betterReplacements.{@link #replaceAll(String) replaceAll}(text);
 *     }
 *   </pre>
 * </blockquote>
 *
 * Internally, an ordered list of {@link Matcher}s and its associated replacement is built as the {@link #append} method is called.<br/>
 * Each matcher is {@link Matcher#reset(CharSequence) reset} with the input specified in the {@link #replaceAll(String)} method.</p>
 * Use of this class can improve performance if the sequence of replacements is intended to be used repeatedly throughout the life of an application.<br/>
 * This is due to the fact that each {@link Pattern} is only compiled once and each {@link Matcher} is only generated once.
 */
public class ReplaceSequence {

  /**
   * Creates a new <code>ReplaceSequence</code> with the first pattern to be replaced with an empty <code>String</code>
   * @param firstPattern The regex {@link Pattern pattern} string for the first replacement
   * @return a new instance
   */
  public static ReplaceSequence create(String firstPattern) {
    return create(firstPattern, string.empty);
  }

  /**
   * Creates a new <code>ReplaceSequence</code> with the first pattern to be replaced with the specified <code>replaceWith</code> parameter.
   * @param firstPattern The regex {@link Pattern pattern} {@link String} for the first replacement
   * @param replaceWith The {@link String} to replace matches of the specified pattern
   * @return a new instance
   */
  public static ReplaceSequence create(String firstPattern, String replaceWith) {
    ReplaceSequence result = new ReplaceSequence(StringReplacement.compile(firstPattern, replaceWith));
    return result;
  }

  /**
   * Appends a new pattern to this instance in a builder pattern
   * @param pattern The regex {@link Pattern pattern} {@link String} for this replacement
   * @return this instance of itself for use in a builder pattern
   */
  public ReplaceSequence append(String pattern) {
    return append(pattern, string.empty);
  }

  /**
   * Appends a new pattern to this instance in a builder pattern
   * @param pattern The regex {@link Pattern pattern} {@link String} for this replacement
   * @param replaceWith The {@link String} to replace matches of the specified pattern
   * @return this instance of itself for use in a builder pattern
   */
  public ReplaceSequence append(String pattern, String replaceWith) {
    replacements.add(StringReplacement.compile(pattern, replaceWith));
    return this;
  }

  /**
   * Applies each of the replacements specified via the initial {@link #create(String)} and/or any additional via {@link #append(String)}
   * @param input the {@link String} to apply all of the replacements to
   * @return the resulting {@link String} after all replacements have been applied
   */
  public String replaceAll(String input) {
    if (string.isNullOrEmpty(input)) return string.empty;
    for (StringReplacement rp : replacements) {
      input = rp.replaceAll(input);
    }

    return input;
  }

  // shhhhh... it's private!
  private List<StringReplacement> replacements = new ArrayList<StringReplacement>();

  private ReplaceSequence(StringReplacement pair) {
    replacements.add(pair);
  }

}


