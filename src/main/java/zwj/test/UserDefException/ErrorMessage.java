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

package zwj.test.UserDefException;

/**
 * 表示ODPS RESTful API返回的出错信息
 */
public class ErrorMessage {

  private String errorcode;

  private String message;

  private String requestId;

  public String HostId;

  public String getErrorcode() {
    return errorcode;
  }

  public String getMessage() {
    return message;
  }

  public String getRequestId() {
    return requestId;
  }

  public String getHostId() {
    return HostId;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("RequestId=").append(requestId).append(',');
    sb.append("Code=").append(errorcode).append(',');
    sb.append("Message=").append(message);
    return sb.toString();
  }
}
