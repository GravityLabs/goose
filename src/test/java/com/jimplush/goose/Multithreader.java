package com.jimplush.goose;

import com.jimplush.goose.texthelpers.string;

import java.util.concurrent.CountDownLatch;

/** Created by IntelliJ IDEA. User: robbie Date: 5/17/11 Time: 3:12 PM */

public class Multithreader {
  private Multithreader() {
  }

  public static RunResult[] run(final Runnable[] runners) {
    final int numThreads = runners.length;
    final CountDownLatch finishLine = new CountDownLatch(numThreads);
    final CountDownLatch startingGate = new CountDownLatch(numThreads);
    final Thread[] threads = new Thread[numThreads];
    final RunResult[] results = new RunResult[numThreads];

    for (int threadIdx = 0; threadIdx < numThreads; threadIdx++) {
      results[threadIdx] = new RunResult(string.empty, false, null);
      final RunResult result = results[threadIdx];
      final int threadNum = threadIdx + 1;
      final Runnable runner = runners[threadIdx];
      threads[threadIdx] = new Thread(new Runnable() {
        public void run() {
          startingGate.countDown();
          try {
            startingGate.await();
            runner.run();
            result.succeeded = true;
          } catch (InterruptedException e) {
            // ignore
          } catch (Exception ex) {
            result.succeeded = false;
            result.thrown = ex;
            result.message = String.format("Thread %d of %d threw this exception: %s", threadNum, numThreads, ex.getMessage());
          } finally {
            finishLine.countDown();
          }
        }
      }, "Multithreader-Thread-" + threadNum);
    }

    for (int i = 0; i < threads.length; i++) {
      Thread thread = threads[i];
      thread.start();
    }

    try {
      finishLine.await();
    } catch (InterruptedException e) {
      // ignore
    }

    return results;
  }

  public static class RunResult {
    private String message;
    private boolean succeeded;
    private Throwable thrown;

    public RunResult(String message, boolean succeeded, Throwable thrown) {
      this.message = message;
      this.succeeded = succeeded;
      this.thrown = thrown;
    }

    public String getMessage() {
      return message;
    }

    public boolean didThisSucceed() {
      return succeeded;
    }

    public Throwable getThrown() {
      return thrown;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder(message);
      if (false == string.isNullOrEmpty(message)) sb.append("\n");
      if (succeeded) {
        sb.append("Status: Succeeded!");
      } else {
        sb.append("Status: Failed!");
      }

      if (thrown != null) {
        sb.append("\nFailure caused by the following exception:\n").append(thrown.getMessage());
        final StackTraceElement[] stackTrace = thrown.getStackTrace();
        if (stackTrace != null) {
          for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement stack = stackTrace[i];
            sb.append("\n").append(stack.toString());
          }
        }
      }

      return sb.toString();
    }
  }
}


