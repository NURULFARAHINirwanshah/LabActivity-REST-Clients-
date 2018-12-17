import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class LabAPI {

	private JFrame frame;
	private JTextField textField_word;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabAPI window = new LabAPI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LabAPI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 584, 404);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblURL = new JLabel("URL : ");
		lblURL.setBounds(57, 77, 37, 16);
		frame.getContentPane().add(lblURL);
		
		textField_word = new JTextField();
		textField_word.setBounds(94, 74, 326, 22);
		frame.getContentPane().add(textField_word);
		textField_word.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(94, 138, 409, 123);
		frame.getContentPane().add(textArea);
		
		JButton btnGo = new JButton("Go!");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(){
					public void run(){
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("url", textField_word.getText()));
						
						String strUrl = "https://api.microlink.io/?";
					
						JSONObject jsonObj = makeHttpRequest(strUrl,"GET",params);try
						{
							String title = jsonObj.getJSONObject("data").getString("title");
							String publisher = jsonObj.getJSONObject("data").getString("publisher");
							String lang = jsonObj.getJSONObject("data").getString("lang");
							String url = jsonObj.getJSONObject("data").getString("url");
					
							String strSetText = "Website title :"+title+" || Website publisher: "+publisher+" || Website language : "+lang+" || Website url : "+url;
					
							textArea.setText(strSetText);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						
			}
					public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
					{
						InputStream is = null;
						String json = "";
						JSONObject jobj = null;
						//making HTTP request
						
						try 
						{
							//check for request method
							if(method == "POST")
							{
								//request method is POST
								//defaultHttpClient
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(url);
								httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));
								
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET")
							{
								//request method is GET
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								url += "?" + paramString;
								HttpGet httpGet = new HttpGet(url);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader (new InputStreamReader(is, "iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							
									while((line = reader.readLine()) != null)
									{
										sb.append(line + "\n");
									}
									is.close();
									json = sb.toString();
									
									jobj = new JSONObject(json);
									
						}
						catch (JSONException e)
						{
							try
							{
								JSONArray jsnArr = new JSONArray(json);
								jobj = jsnArr.getJSONObject(0);
							}
							catch(JSONException e1)
							{
								e1.printStackTrace();
							}	
						}
						catch (Exception ee)
						{
							ee.printStackTrace();
						}
						
						return jobj;
					}
				};
				thread.start();
			}
		});
		btnGo.setBounds(430, 73, 73, 25);
		frame.getContentPane().add(btnGo);
	}
}