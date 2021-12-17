package Operation;

import java.net.*;
import java.util.*;
import org.json.JSONObject;
import Model.Model;
import Model.Product;

import java.io.*;

public class JsonParsing {

	static String parseJsonString(String urlPath) {
		String res="";
		try {
			URL url=new URL(urlPath);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			int responseCode=conn.getResponseCode();
			if(responseCode==200) {
				InputStream is=conn.getInputStream();
				StringBuffer sb=new StringBuffer();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String line=br.readLine();
				while(line!=null) {
					res=res+"\n"+line;
					line=br.readLine();
				}
			}
			else {
				System.out.println("Response Code:"+responseCode);
			}
		}
		catch(Exception e) {
			System.out.println("Exception Occured in parseJsonString:"+e.getMessage());
		}
		return(res);
	}
	public static void main(String[] args) {
	
		try {
			String json=parseJsonString(Model.path);
			JSONObject jsonObj=new JSONObject(json);
			ArrayList<Product> productDetailList=new ArrayList<Product>();
			int productCount=jsonObj.getInt("count");
			JSONObject products=jsonObj.getJSONObject("products");
			JSONObject element=null;
			Iterator<?> it=products.keys();
			while(it.hasNext()) {
				String productCode=(String)it.next();
				element=products.getJSONObject(productCode);
				Product productObj=new Product();
				productObj.setProductCode(Integer.valueOf(productCode));
				productObj.setSubcategory(element.getString("subcategory"));
				productObj.setTitle(element.getString("title"));
				productObj.setPrice(Integer.valueOf(element.getString("price")));
				productObj.setPopularity(Integer.valueOf(element.getString("popularity")));
				productDetailList.add(productObj);
			}

			Collections.sort(productDetailList,Comparator.comparingInt(Product::getPopularity).reversed());
			for(Product p:productDetailList) {
				System.out.println(p.toString());
			}
			System.out.println("Count="+productDetailList.size());
		}
		catch(Exception e) {
			System.out.println("Exception Occured in main function:"+e.getMessage());
		}
		
	}

}
