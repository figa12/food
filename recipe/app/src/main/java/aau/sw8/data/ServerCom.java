package aau.sw8.data;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import aau.sw8.recipe.MainActivity;

/**
 * Created by jacob on 3/31/14.
 */


public class ServerCom {

    /*Variables*/
    private Context context;
    private static ServerCom instance = new ServerCom();

    /*Constructors*/
    private ServerCom() {}

    public static ServerCom getInstance(){
        return instance;
    }

    /*Class methods*/
    public void init(Context context){
        if(context instanceof MainActivity){
            this.context = context;
        }
    }

    public void startNewTask(BasicNameValuePair... pairs){
        new ServerComTask(this.context).execute(pairs);
    }

    public void startNewTask(Context context, BasicNameValuePair... pairs){
        new ServerComTask(context).execute(pairs);
    }

    /*Private Class*/
    private class ServerComTask extends AsyncTask<BasicNameValuePair, Integer, String>{
        /*Variables*/
        private Context context;
        private final String serverUrl = "http://figz.dk/food/test.php";

        /*Constructors*/
        public ServerComTask(Context context) {
            this.context = context;
        }

        /*Override methods*/
        @Override
        protected String doInBackground(BasicNameValuePair... pairs) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(Arrays.asList(pairs));
            //Create the http request
            HttpParams httpParams = new BasicHttpParams();

            //Setup timeouts
            HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
            HttpConnectionParams.setSoTimeout(httpParams, 15000);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(this.serverUrl);

            String result = "Timeout";

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);

            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //TODO Should not always call onServerTest
            MainActivity mainActivity = null;
            if(this.context instanceof MainActivity){
                mainActivity = (MainActivity) this.context;
            }

            if (result == null || result.equals("") && mainActivity != null) {
                mainActivity.onServerTest("Failed");
            }else{
                mainActivity.onServerTest(result);
            }
        }

        /*Class methods*/
    }
}


