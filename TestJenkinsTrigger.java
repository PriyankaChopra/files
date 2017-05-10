import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TestJenkinsTrigger {

	public static void main(String[] args) {
		try {

			URL url = new URL("http://admin:f78598af09db161f9c5eea94774f2553@jenkins-orch.vici.verizon.com:8080/crumbIssuer/api/xml?xpath=concat\\(//crumbRequestField,%22:%22,//crumb\\)");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			String input = "{\"data\": [{\"key\": \"username\",\"value\": \"admin\",\"type\": \"text\",\"enabled\": true},+"
					+ "{\"key\": \"password\",\"value\": \"fortinet\",\"type\": \"text\",\"enabled\": true}],\"dataMode\": \"urlencoded\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			/*for (Map.Entry<String, List<String>> k : conn.getHeaderFields().entrySet()) {
			    for (String v : k.getValue()){
			         System.out.println(k.getKey() + ":" + v);
			    }
			}*/
			
			List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
			String cookie = "";
			for(String str : cookies){
				cookie = cookie+" "+str;
			}
			System.out.println(cookie);
			
			System.out.println(conn.getHeaderField("Set-cookie"));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
			
			conn.disconnect();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
