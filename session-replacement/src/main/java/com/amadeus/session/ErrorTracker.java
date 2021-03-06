package com.amadeus.session;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class is used to determine if the application is in error or not
 * 
 * If the number of error stored during the delay reach the max , the application is in error
 */

public class ErrorTracker {
  /**
   * Standard constructor
   * 
   * @param period
   *          Time in milliseconds while items are kept
   * @param max
   *          If the number of items is more than the max value the Tracker is considered in error
   */
  public ErrorTracker(int period, int max) {
    this.period = period;
    this.max = max;
  }

  private ConcurrentLinkedQueue<Long> list = new ConcurrentLinkedQueue<Long>();

  /**
   * When a new event it added , all the event that is older that ( event time - period ) are removed from the tracker
   */
  final int period;

  /**
   * After the limits the Tracker is considering in error
   */
  final int max;

  /**
   * The parameter is a time with the format millisecond from 1900 (<code>System.currentTimeMillis()</code>). This 
   * method adds an event into the Tracker and remove all the old events with the following criteria
   * <code>now - oldevent &gt; period</code> will be removed
   * 
   * @param now
   */

  public void addError(long now) {
    list.add(new Long(now));
    boolean cont = true;
    while (cont) {
      Long last = list.peek();
      if (now - last > period) {
        list.poll();
      } else {
        cont = false;
      }
    }
  }

  public boolean reachLimits() {
    return list.size() >= max;
  }

  public int size() {
    return list.size();
  }

  public void reset() {
    list.clear();
  }
}
