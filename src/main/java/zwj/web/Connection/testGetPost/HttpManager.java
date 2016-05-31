/*   Copyright (C) 2013-2014 Computer Sciences Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package zwj.web.Connection.testGetPost;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

public class HttpManager {

	private static final PoolingHttpClientConnectionManager cm;
	static {
		try {
			cm = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			cm.setMaxTotal(200);
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(20);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private HttpManager() { }
	
	public static HttpClient getClient() {
		return HttpClients
				.custom()
				.setConnectionManager(cm)
				.build();
	}

	public static String post(HttpPost post) throws Exception {
        final HttpClient client = HttpManager.getClient();
        final HttpResponse response = client.execute(post);
        final HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
	}

    public static String get(HttpGet get) throws Exception {
        final HttpClient client = HttpManager.getClient();
        final HttpResponse response = client.execute(get);
        final HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

}
