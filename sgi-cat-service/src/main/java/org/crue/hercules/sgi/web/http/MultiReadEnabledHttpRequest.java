package org.crue.hercules.sgi.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * MultiReadEnabledHttpRequest
 * 
 * {@link HttpServletRequestWrapper} that can be enabled for reading request
 * content multiple times.
 */
public class MultiReadEnabledHttpRequest extends HttpServletRequestWrapper implements MultiReadEnabled {
  private String content;
  private boolean multiReadEnabled = false;

  public MultiReadEnabledHttpRequest(HttpServletRequest request) throws IOException {
    super(request);
  }

  public MultiReadEnabledHttpRequest(HttpServletRequest request, boolean enableMultiRead) throws IOException {
    super(request);
    this.multiReadEnabled = enableMultiRead;
  }

  /**
   * Gets the original request ServletInputStream or a new ServletInputStream for
   * the cached data if multiRead is enabled.
   * 
   * @return ServletInputStream
   * @throws IOException if there is an IO error
   */
  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (multiReadEnabled) {
      String content;
      try {
        content = getContent();
      } catch (Exception e) {
        throw new IOException(e);
      }
      final byte[] myBytes = content.getBytes("UTF-8");
      ServletInputStream servletInputStream = new ServletInputStream() {
        private int lastIndexRetrieved = -1;
        private ReadListener readListener = null;

        @Override
        public boolean isFinished() {
          return (lastIndexRetrieved == myBytes.length - 1);
        }

        @Override
        public boolean isReady() {
          // This implementation will never block
          // We also never need to call the readListener from this method, as this method
          // will never return false
          return isFinished();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
          this.readListener = readListener;
          if (!isFinished()) {
            try {
              readListener.onDataAvailable();
            } catch (IOException e) {
              readListener.onError(e);
            }
          } else {
            try {
              readListener.onAllDataRead();
            } catch (IOException e) {
              readListener.onError(e);
            }
          }
        }

        @Override
        public int read() throws IOException {
          int i;
          if (!isFinished()) {
            i = myBytes[lastIndexRetrieved + 1];
            lastIndexRetrieved++;
            if (isFinished() && (readListener != null)) {
              try {
                readListener.onAllDataRead();
              } catch (IOException ex) {
                readListener.onError(ex);
                throw ex;
              }
            }
            return i;
          } else {
            return -1;
          }
        }
      };
      return servletInputStream;
    } else {
      HttpServletRequest request = (HttpServletRequest) getRequest();
      ServletInputStream inputStream = request.getInputStream();
      return inputStream;
    }
  }

  /**
   * @return BufferedReader
   * @throws IOException if there is an IO error
   */
  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  /**
   * @return String
   * @throws Exception if there is a problem retrieving the request content
   */
  protected String getContent() throws Exception {
    if (this.content == null) {
      HttpServletRequest request = (HttpServletRequest) getRequest();
      StringBuilder stringBuilder = new StringBuilder();
      BufferedReader bufferedReader = null;
      try {
        InputStream inputStream = request.getInputStream();
        if (inputStream != null) {
          bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
          char[] charBuffer = new char[128];
          int bytesRead = -1;
          while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
            stringBuilder.append(charBuffer, 0, bytesRead);
          }
        } else {
          stringBuilder.append("");
        }
      } catch (IOException ex) {
        throw ex;
      } finally {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      }
      // Store request content in 'content' variable
      this.content = stringBuilder.toString();
    }
    return this.content;
  }

  public void enableMultiRead() {
    this.multiReadEnabled = true;
  }

  /**
   * @return boolean
   */
  public boolean isMultiReadEnabled() {
    return this.multiReadEnabled;
  }

}