/*
 * The MIT License
 *
 * Copyright 2017 Red Hat inc. and individual contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jenkins.plugins.logstash;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import groovy.lang.Binding;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is handling custom groovy script processing of JSON payload.
 * Each call to process executes the script provided in job configuration.
 * Script is executed under the same binding each time so that it has ability
 * to persist data during build execution if desired by script author.
 * When build is finished, script will receive null as the payload and can
 * return any cached but non-sent data back for persisting.
 * The return value of script is the payload to be persisted unless null.
 */
public class LogstashScriptProcessor implements LogstashPayloadProcessor{

  @Nonnull
  private final OutputStream consoleOut;


  public LogstashScriptProcessor(OutputStream consoleOut) {
    this.consoleOut = consoleOut;
  }

  /**
   * Helper method to allow logging to build console.
   */
  @SuppressFBWarnings(
    value="DM_DEFAULT_ENCODING",
    justification="TODO: not sure how to fix this")
  private void buildLogPrintln(Object o) throws IOException {
    consoleOut.write(o.toString().getBytes());
    consoleOut.write("\n".getBytes());
    consoleOut.flush();
  }

  @Override
  public JSONObject finish() throws Exception {
    buildLogPrintln("Tearing down Script Log Processor..");
    return null;
  }

}
