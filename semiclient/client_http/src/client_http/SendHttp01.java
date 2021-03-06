package client_http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class SendHttp01 {

	
	class SendThread extends Thread{

		double temp;
		double humi;
		
		String urlstr = "http://192.168.0.7/iot1.mc";
		URL url = null;
		HttpURLConnection con = null;

		BufferedReader br = null;
		
		public SendThread() {
			
		}
		
		public SendThread(double temp, double humi) {
			this.temp = temp;
			this.humi = humi;
		}
		
		@Override
		public void run() {
			try {
				
				url = new URL(urlstr + "?temp="+temp+"&humi="+humi);
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(5000);
				con.setRequestMethod("POST");
				con.getInputStream();

				br = new BufferedReader(
						new InputStreamReader(
								con.getInputStream()));

				String str = "";
				//str = br.readLine();
				//System.out.println(str);
				while ((str = br.readLine()) != null) {
					if(str.equals("")) {
						continue;
					}
					System.out.println(str.trim());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.disconnect();
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		
		
	}
	
	
	
	public void sendData(double temp, double humi) {
		SendThread st = new SendThread(temp, humi);
		st.start();
	}
	
	public static void main(String[] args) {		
		while(true) {
			Random r = new Random(100);
			int t = r.nextInt();
			int h = r.nextInt();
			SendHttp01 shttp = new SendHttp01();
			shttp.sendData(t,h);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
