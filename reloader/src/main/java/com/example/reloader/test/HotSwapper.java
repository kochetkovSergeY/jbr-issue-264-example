/*
 * Javassist, a Java-bytecode translator toolkit.
 * Copyright (C) 1999- Shigeru Chiba. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License.  Alternatively, the contents of this file may be used under
 * the terms of the GNU Lesser General Public License Version 2.1 or later,
 * or the Apache License Version 2.0.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */

package com.example.reloader.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class HotSwapper {
  private VirtualMachine jvm;


  @SuppressWarnings("unchecked")
  public HotSwapper(String port, String host)
      throws IOException, IllegalConnectorArgumentsException {
    jvm = null;
    AttachingConnector connector = (AttachingConnector) findConnector("com.sun.jdi.SocketAttach");
    Map<String, Connector.Argument> arguments = connector.defaultArguments();
    arguments.get("hostname").setValue(host);
    arguments.get("port").setValue(port);

    jvm = connector.attach(arguments);
  }

  private Connector findConnector(String connector) throws IOException {
    List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();

    for (Object candidate : connectors) {
      Connector con = (Connector) candidate;
      if (con.name().equals(connector)) {
        return con;
      }
    }

    throw new IOException("Not found: " + connector);
  }

  /**
   * Reloads a class.
   */
  public void reload(Map<String, byte[]> classFiles) {
    Map<ReferenceType, byte[]> map = new HashMap<>();

    for (Map.Entry<String, byte[]> stringEntry : classFiles.entrySet()) {
      ReferenceType type = toRefType(stringEntry.getKey());
      map.put(type, stringEntry.getValue());

    }
    hotswap(map);
  }

  private ReferenceType toRefType(String className) {
    System.out.println();
    List<ReferenceType> referenceTypes = jvm.classesByName(className);
    AtomicInteger count = new AtomicInteger(0);

    referenceTypes.forEach(referenceType -> {
      try {
        referenceType.toString();
      }
      catch (Exception e) {
        System.out.println("Rtype toString error");
        e.printStackTrace();
      }
    });

    return referenceTypes.stream()
        .filter(type -> {
          try {
            type.toString();
            return true;
          }
          catch (ObjectCollectedException ex) {
            return false;
          }
        })
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Couldn't reload collected class=" + className));
  }

  private void hotswap(Map<ReferenceType, byte[]> map) {
    jvm.redefineClasses(map);
  }

  public void dispose() {
    jvm.dispose();
  }
}
