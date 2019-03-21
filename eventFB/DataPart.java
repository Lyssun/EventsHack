package eventFB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataPart {
	
	// Change USER_CREDENTIALS ...
	
	public String getDataAccess() throws IOException {
		String accessToken = "";
		String url = USER_CREDENTIALS;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
	    int responseCode = con.getResponseCode();
	    System.out.println("\nSending 'GET' request to URL : " + url);
	    System.out.println("Response Code : " + responseCode);
	    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
	    {
	     	response.append(inputLine);
	    }
	    in.close();
	    System.out.println(response);
	    JSONObject myResponse = new JSONObject(response.toString());
	    accessToken = myResponse.getString("access_token");
		return accessToken;
	}
	
	public void loadEventFromFB(String access_token, String page_id, Session session) throws IOException
	{

		String url = "https://graph.facebook.com/v3.1/"+page_id+"/events/?fields=category%2Ctype%2Cend_time%2Cstart_time%2Cdescription%2Cplace%2Ccover%2Cattending_count%2Cticket_uri%2Cticket_uri_start_sales_time%2Cname&access_token="+access_token;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
	    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
	    {
	     	response.append(inputLine);
	    }
	    in.close();
	    System.out.println(response);
	    
	    JSONObject myResponse = new JSONObject(response.toString());
	    
	    JSONArray eventArray = myResponse.getJSONArray("data");

	    Transaction t = session.beginTransaction();
	    ArrayList<OrganisedEvent> arrayOrgaEv = new  ArrayList<OrganisedEvent>();
	    eventArray.forEach(item ->
	    {

		    
		    
	    	OrganisedEvent orgEv = new OrganisedEvent();
	    	
	    	JSONObject test = (JSONObject) item;
	    	orgEv.setName(test.get("name"));
	    	orgEv.setDescription( test.get("description"));
	    	orgEv.setStart_time(test.getString("start_time"));
	    	orgEv.setHref("https://www.facebook.com/events/"+test.get("id"));
	    	JSONObject place = test.getJSONObject("place");
	    	JSONObject loc = place.getJSONObject("location");

	    	try {
		    	orgEv.setLieu(loc.get("street"));
		    	orgEv.setVille(loc.get("city"));
			} catch (JSONException e) {
				System.err.println("Aucune ville.");
			}
	    	orgEv.setLatitude(loc.getFloat("latitude"));
	    	orgEv.setLongitude(loc.getFloat("longitude"));
	    	
	    	try
	    	{
		    	JSONObject cover = test.getJSONObject("cover");
		    	orgEv.setImage(cover.get("source"));
		    	
	    	}
	    	catch(JSONException e)
	    	{
	    		System.out.println("No Picture for this");
	    	}


	    	orgEv.setSource("facebook");
	    	orgEv.toString();
	    	arrayOrgaEv.add(orgEv);

	    });
	    
	    
	    String urlOrganisator = "https://graph.facebook.com/v3.1/"+page_id+"?fields=category%2Cname%2Cpicture%7Burl%7D&access_token="+access_token;
		URL objOrg;
		Object categorie = null;
		Object imageOrg = null;
		Object nameOrg = null;
		try {
			objOrg = new URL(urlOrganisator);


			HttpURLConnection conOrg = (HttpURLConnection) objOrg.openConnection();
			conOrg.setRequestMethod("GET");
			conOrg.setRequestProperty("Accept", "application/json");
		    BufferedReader inOrg = new BufferedReader( new InputStreamReader(conOrg.getInputStream()));
		    String inputLineOrg;
		    StringBuffer responseOrg = new StringBuffer();
		    while ((inputLineOrg = inOrg.readLine()) != null)
		    {
		    	responseOrg.append(inputLineOrg);
		    }
		    inOrg.close();
		    
		    JSONObject resultOrg = new JSONObject(responseOrg.toString());
		    categorie = (resultOrg.get("category"));
		    nameOrg = resultOrg.get("name");
		    imageOrg = (resultOrg.getJSONObject("picture").getJSONObject("data").getString("url"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	Object categorieFinal = categorie;
    	Object imageOrgFinal = imageOrg;
	    Object nameOrgFinal = nameOrg;
    	arrayOrgaEv.forEach(
    			e -> {
    				e.setOrganisation(nameOrgFinal);
    				e.setCategorie(categorieFinal);
					if(e.getImage() == "")
						e.setImage(imageOrgFinal);
					session.persist(e);
    			});
    	t.commit();
	}	
	
	
	public void LoadEventFromBE(Session session, String token, double latitude, double longitude, int range, int page) throws IOException
	{
	

		String url = "https://www.eventbriteapi.com/v3/events/search/?token="+token+"&expand=organizer%2Cvenue%2Clogo,category&location.latitude="+latitude+"&location.longitude="+longitude+"&location.within="+range+"km&sort_by=date&page="+page;
		System.out.println(url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
	    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
	    {
	     	response.append(inputLine);
	    }
	    in.close();
	    System.out.println(response);
	    JSONObject myResponse = new JSONObject(response.toString());
	    
	    JSONArray eventArray = myResponse.getJSONArray("events");
		 

	    Transaction t = session.beginTransaction();
	    eventArray.forEach(item ->
	    {
	    	OrganisedEvent orgEv = new OrganisedEvent();
	    	JSONObject test = (JSONObject) item;
	    	orgEv.setName(test.getJSONObject("name").get("text"));	    	
	    	orgEv.setDescription(test.getJSONObject("description").get("text"));
	    	orgEv.setStart_time(test.getJSONObject("start").getString("local"));
	    	orgEv.setHref(test.get("url"));
	    	try {
		    	orgEv.setCategorie(test.getJSONObject("category").get("name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	try {
	    	JSONObject venue = test.getJSONObject("venue");
	    	JSONObject address = venue.getJSONObject("address");

    		orgEv.setVille(address.get("city"));
	    	orgEv.setLieu(address.get("address_1"));
	    	orgEv.setLatitude(address.getFloat("latitude"));
	    	orgEv.setLongitude(address.getFloat("longitude"));
	    	}
			catch (JSONException e) {
				e.printStackTrace();
			}
	    	orgEv.setSource("Event Brite");
	    	orgEv.setOrganisation(test.getJSONObject("organizer").get("name"));
	    	try {
	    		orgEv.setImage(test.getJSONObject("logo").get("url"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			session.persist(orgEv);

	});
    	t.commit();
	}
}
