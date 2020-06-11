package org.crue.hercules.sgi.web.http;

/**
 * MultiReadEnabled
 * 
 * Tella an Object can be enabled to read multiple times.
 */
public interface MultiReadEnabled {
  void enableMultiRead();

  boolean isMultiReadEnabled();
}