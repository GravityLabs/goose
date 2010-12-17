package com.jimplush.goose.network; /**
 * User: jim
 * Date: 12/16/10
 */

/**
 * Thrown when we've reached the max number of bytes we want to process for a given HTML page
 * some html pages are janky with tons of spam on them from over the years.
 */
public class MaxBytesException extends Exception {

}
