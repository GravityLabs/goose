package com.jimplush.goose.texthelpers; /**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/17/11
 * Time: 2:11 PM
 */

import com.jimplush.goose.Multithreader;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StringReplacementTest extends TestCase {
  private static final Logger logger = LoggerFactory.getLogger(StringReplacementTest.class);

  private static final StringReplacement singletonReplacement = StringReplacement.compile("\\s", "_SPACE_");

  public void testMulitithreadedReplacements() {
    int numThreads = 10;

    final String[] inputs = {
        "Here is some text containing whitespace to use in our test.",
        "This would be a different bit of text.",
        "Little One",
        "a b c",
        "oefioe  eoiwjf ojiewf  eiwoj  ewjo",
        "ejie  i  ij j j  ij fioffjioei f",
        "ee e ee eee e e e",
        "    l l l lll",
        "nnnnnnnnnnnnnnnnnnn n n nnn nnn",
        "n    n n n nnnn n nn n n n n n n nnn n"
    };

    Runnable[] runners = new Runnable[numThreads];

    for (int i = 0; i < numThreads; i++) {
      final String input1 = inputs[0];
      int randSeed = getRandomIntBetween(inputs.length - 1, i);
      final String input2 = inputs[getRandomIntBetween(inputs.length - 1, randSeed)];
      final String input3 = inputs[getRandomIntBetween(inputs.length - 1, randSeed * (i + 1))];
      runners[i] = new Runnable() {
        public void run() {
            for (int j = 0; j < 1000; j++) {
              singletonReplacement.replaceAll(input1);
              singletonReplacement.replaceAll(input2);
              singletonReplacement.replaceAll(input3);
            }
        }
      };
    }

    Multithreader.RunResult[] results = Multithreader.run(runners);

    for (int i = 0; i < results.length; i++) {
      Multithreader.RunResult result = results[i];
      assertTrue(String.format("Not all threads succeeded! Thread: %d of %d failed!%n%s", i + 1, numThreads, result), result.didThisSucceed());
    }

    logger.info("All Done!");
  }

  private static int getRandomIntBetween(int max, int seed) {
    Random generator = new Random(seed * 100000);
    return generator.nextInt(max);
  }
}


