/**
 * 
 */

/**
 * @author 0000100363
 *
 */
package firstframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
 
public class TestGoogleSea {
 
	public static void main(String[] args) throws Throwable {
 
		String query = "How I met your Mother season 3 english subtitles";
		TestGoogleSea subs= new TestGoogleSea();
		//if(query.endsWith(null)){
		if(query.equals(null)){
			System.out.println("null query, please provide an input");
			System.exit(0);
		}
		else
			subs.Fetch(query);
	}
	
	public void Fetch(String query) throws IOException, Throwable {
		
		// deleting a existing file
				try{
					 
		    		File file = new File("test.html");
		 
		    		if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
		    		
		    		 file = new File("try.zip");
		   		 
		    		if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("for try.zip, Delete operation is failed or file does not exits.");
		    		}
		    		 file = new File("try.html");
			   		 
			    		if(file.delete()){
			    			System.out.println(file.getName() + " is deleted!");
			    		}else{
			    			System.out.println("for try.zip, Delete operation is failed or file does not exits.");
			    		}
		 
		    	}catch(Exception e){
		 
		    		e.printStackTrace();
		 
		    	}
				///////////////// done deleting
				

		String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		//String query = "How I met your Mother season 8 English subtitles";
		String charset = "UTF-8";
 
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
 
		int total = results.getResponseData().getResults().size();
		System.out.println("total: "+total);
		// check if links are found or not.....
		if(total <= 0){
			System.out.println("no URL found, try again with different query");
			System.exit(0);
		}
		
		System.out.println("*****************************************************");
		System.out.println("The fatched URLs are---->\n"); // one issues is that only 4 URLs get fetched, may be issue of some custom code.
		for(int i=0; i<=total-1; i++){
			System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
			System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");
 
		}
		System.out.println("*****************************************************");
		// URL find
		String subURL = null;
		pattern_matching p = new pattern_matching();
		String[] sub = {"subscene.com/", "tvsubtitles.net/"};// tvsubtitles.net is the one causing issue, 
										//it replied HTTP 302 with relative link (with spaces)in the Location tag
		for(int i=0; i<=total-1; i++){
			String Y = results.getResponseData().getResults().get(i).getUrl().toString();
		//	System.out.println(Y);
			int found =0;
			
			for(int k = 0; k<sub.length; k++){
			if (p.SubString(Y, sub[k])){
				System.out.println("Link seems maching is: " + Y);
				subURL = Parse(Y, sub[k]);
				//temp = subURL;
				subURL = "http://" + sub[k] + subURL;   // making the full URL here
				found =1;
				break;
			}
			}
			if(found ==1)
				break;
			if(i == total-1){
				System.out.println("URL not found, try again with different query");
				System.exit(0);
			}
		
		}
		System.out.println("link is found");
		System.out.println("modified complete link is: "+ subURL); // current link to pursue 
		//--------------------------------------------
		// Check for error, if link has spaces or not,,,, etc.... remove them,,,
		subURL = subURL.trim();
		//subURL = subURL.replaceAll("\\s+", "%20");
		subURL = subURL.replaceAll("[ ]+ ", "%20");
		subURL = new URI(null, subURL, null).toASCIIString();
		URL url2 = new URL(subURL);
		HttpURLConnection con;
		
		// HttpURLConnection con = (HttpURLConnection) url2.openConnection();
		// some how sanitize the link, here,,,,
		// the issue is that, if the connection is back to back how to tell java to sanitize it automatically.
		//InputStream streamx = (con = (HttpURLConnection) url2.openConnection()).getInputStream(); 
		HttpURLConnection.setFollowRedirects(false);
		if(p.SubString(subURL, "tvsubtitles")){
		con =  (HttpURLConnection) url2.openConnection();
		while(con.getResponseCode()==301){
			System.out.println("Key" );
			con.disconnect();
		//	con.set
			String temp = url2.toString();
			temp = temp.substring(23, temp.length());
			System.out.println("we have this:" + temp);
			url2 = new URL("http://www.tvsubtitles.net/" + temp);
			con =  (HttpURLConnection) url2.openConnection();
			//con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			con.addRequestProperty("Host", "www.tvsubtitles.net");
			con.addRequestProperty("Cookie", "visited=1; visited1=1");
			}
			
		
	//	con.setInstanceFollowRedirects(false);
		Map<String, List<String>> lo = con.getHeaderFields();
		String loc = null;
		//	System.out.println(lo.keySet().toString());
		for (Map.Entry<String, List<String>> entry : lo.entrySet()) {
			System.out.println("Key : " + entry.getKey() + 
	                 " ,Value : " + entry.getValue());
		}
		if(lo.containsKey("Location")){
			loc = lo.get("Location").toString();
			System.out.println("here it is,,,ohohokhohkhohko" + loc);
			loc = loc.substring(0, loc.length());
			loc = loc.replaceAll("[ ]+", "%20");
			subURL = "http://www.tvsubtitles.net/" + loc.substring(1, loc.length()-1);
			System.out.println("here it is,,,ohohokhohkhohko" + subURL);
		}
		
		//System.out.println("here it is,,,ohohokhohkhohko" + loc);
		}
		url2 = new URL(subURL);
		//System.out.println("here it is,,,ohohokhohkhohko");
		con = (HttpURLConnection) url2.openConnection();
		//con.addRequestProperty("Host", "www.tvsubtitles.net");
		con.addRequestProperty("Cookie", "visited=1; visited1=1");
		InputStream streamx = con.getInputStream(); 
		int responseCode = con.getResponseCode();		
		
		System.out.println("here it is,,," + responseCode);
		//File Files = new File();
		String FileName = query + ".zip";// what file to read if we get it,,,,,,
		
	if (responseCode == HttpURLConnection.HTTP_OK) {   // response is HTTP 200,,,, 
			System.out.println("Getting the file......");
			try  { //try (InputStream stream = con.getInputStream()) {
			    Files.copy(streamx, Paths.get("try.zip"));    // since all the subtitles files are zip files, just a name to save it.
			}catch(IOException e){
				e.printStackTrace();
			} 
		System.out.println("File download is done");
		}
/*	else if (responseCode == 302) {                // I tried this loop for tvsubtitles, but it never comes here,
														// due to concurrent request in previous loop
			try (InputStream stream = ((HttpURLConnection) con).getErrorStream()) {
			    Files.copy(stream, Paths.get("test2.html"));
			
			BufferedReader br = new BufferedReader(new FileReader("test2.html")); // agar HTTP 302 detect hota hai to nai file ko save karo
																				// and parse it again to get the location tag wala part
			String sCurrentLine;
			 String Location = null;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(" line is: " + sCurrentLine);
				if(p.SubString(sCurrentLine, "Location")){
					Location = sCurrentLine.substring(11);
					break;
				}
			}
			
			Location = Location.trim();
			Location = Location.replaceAll("[ ]+", "%20");
			for(int i = 0; i<sub.length;i++){
				if(p.SubString(sub[i], "tvsubtitles.net/"))                 // hard coded for tvsutitltes and sbscene for now//////
						Location = "http://tvsubtitles.net/"+Location;
				if(p.SubString(sub[i], "subscene.com/"))					// hard coded for tvsutitltes and sbscene for now//////
					Location = "http://subscene.com/"+Location;
			}
			URL url3 = new URL(Location);
			System.out.println(" the final link is: " + Location);
			HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
			int responseCode3 = con3.getResponseCode();
			if (responseCode3 == HttpURLConnection.HTTP_OK) {
				System.out.println("Getting the file......");
				try (InputStream stream3 = con3.getInputStream()) {  //again, .zip file for subtitles, save it..address..
				    Files.copy(stream3, Paths.get("try.zip"));          // 
					}
				System.out.println("File download is done");
			}else {
				System.out.println("bad response code:" +  responseCode3);
				System.exit(0);
			}
		}
		//inputStream.close();
		}    */
		else {
			System.out.println("bad response code:" +  responseCode);
			System.exit(0);
		}
	}
	
	private URL URL(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String Parse(String X, String sub) throws IOException{
		int flag=1;
		String linkHref = null;
		int count = 0;
	do{
		URL url2 = new URL(X);
		HttpURLConnection con = (HttpURLConnection) url2.openConnection();
		int responseCode = con.getResponseCode();		
		InputStream inputStream = null;
		String test = "test";
		//File Files = new File();
		String FileName = test + ".html";
			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (InputStream stream = con.getInputStream()) {
				    Files.copy(stream, Paths.get(FileName));
				}catch(Exception e){
					InputStream stream = con.getErrorStream();
				    Files.copy(stream, Paths.get(FileName));
				    System.out.println(e.getMessage());
				}
				 //String html = "<html><head><title>First parse</title></head>"+ "<body><a href="+ "/subtitle/download?mac=z4XGKzZi5ZtjWR6jfdeI97XDYN2b4yBreI8TJIBfpdwvw8NHDaWCFJClB5c0ve8U0></a><p>Parsed HTML into a doc.</p></body></html>";
				//reading the file to parse
				File input = new File(FileName);
				System.out.println(X);
				Document doc = Jsoup.parse(input, "UTF-8"); //Document doc = Jsoup.parse(html, String baseUri)
				Elements links = null;
				if(sub.equals("subscene.com/"))
					links = doc.select("a[href^=/subtitle/download?], a[href^=/subtitles/]:contains(english)");
				else if(sub.equals("tvsubtitles.net/"))
					links = doc.select("a[href^=download-], a[href^=/subtitle-], a[href^=episode]");
			
				
				String dummy1 = "/subtitle/download";
				String dummy2 = "download-";
				
				if( (linkHref = links.attr("href")).contains(dummy1) || (linkHref = links.attr("href")).contains(dummy2)){
					flag = 0;	
					System.out.println(" the new link to download is: " + linkHref);
					return linkHref; // "http://example.com/"
				}
				else{
					// deleting a existing file
					try{
						 
			    		File file = new File("test.html");
			 
			    		if(file.delete()){
			    			System.out.println(file.getName() + " is deleted!");
			    		}else{
			    			System.out.println("Delete operation is failed.");
			    		}
			 
			    	}catch(Exception e){
			 
			    		e.printStackTrace();
			 
			    	}
					///////////////// done deleting
					
					linkHref= "http://" + sub + linkHref;
					System.out.println(" the new link to look for is: " + linkHref);
					linkHref= Parse(linkHref, sub);
					flag = 0;
				}
			//inputStream.close();
			} else {
					System.out.println(" HTTP 404 (URL Not Found), please check the query and see if a real website is getting fatched, not a dummy one");
					flag=0;
			}
		}while(flag==1);
			
		//String A = "/subtitle/download?mac=z4XGKzZi5ZtjWR6jfdeI97XDYN2b4yBreI8TJIBfpdwvw8NHDaWCFJClB5c0ve8U0";
		
		return linkHref;
		//return A;
	}
}
 
	// this below code I picked from net, with the corresponding jar files,,,,, for jsoup and json.......

class GoogleResults{
 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
}