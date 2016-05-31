/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 *
 */
package zwj.test.Transports.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 用来构造RESTful API调用的资源标识符
 *
 *
 */
public class ResourceBuilder {
    public static final String LOADPROJECTS = "/api/u/loadProjects";
    public static final String CREATEPROJECTS = "/action/meta/dataProject/api/u/createProject";
    public static final String REMOVEPROJECTS="/action/meta/dataProject/api/updateProject";
    public static String loadProjectResource(){
    return LOADPROJECTS;
  }
    public static String createProjectResource(){
        return CREATEPROJECTS;
    }
//    public static String getRemoveprojects(){
//
//    }

  public static String encode(String str) {
    if (str == null || str.length() == 0) {
      return str;
    }

    String r = null;
    try {
      r = URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException("Encode failed: " + str);
    }
    r = r.replaceAll("\\+", "%20");
    return r;
  }
  }
