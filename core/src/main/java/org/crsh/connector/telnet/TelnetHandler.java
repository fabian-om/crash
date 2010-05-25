/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.crsh.connector.telnet;

import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;
import org.crsh.connector.Term;
import org.crsh.connector.TermShellAdapter;
import org.crsh.shell.Connector;

import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TelnetHandler implements Shell {

  /** . */
  private TermShellAdapter decoder;

  public void run(Connection conn) {

    Connector connector = new Connector(
        TelnetLifeCycle.instance.getExecutor(),
        TelnetLifeCycle.instance.getShellFactory().build());

    //
    Term term = new TelnetTerm(conn);

    //
    decoder = new TermShellAdapter(term, connector);

    //
    conn.addConnectionListener(this);

    //
    try {
      decoder.run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void connectionIdle(ConnectionEvent connectionEvent) {
  }

  public void connectionTimedOut(ConnectionEvent connectionEvent) {
    decoder.close();
  }

  public void connectionLogoutRequest(ConnectionEvent connectionEvent) {
    decoder.close();
  }

  public void connectionSentBreak(ConnectionEvent connectionEvent) {
  }

  public static Shell createShell() {
    return new TelnetHandler();
  }
}
