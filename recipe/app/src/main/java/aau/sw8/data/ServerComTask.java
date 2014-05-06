package aau.sw8.data;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ServerComTask<T> extends AsyncTask<BasicNameValuePair, Integer, T> {
    private final String TAG = "ServerComTask";

    public interface OnResponseListener<T> {
        void onResponse(T result);
        void onFailed();
    }

    public interface ServerAlertDialog {
        AlertDialog getServerAlertDialog();
    }

    /*Variables*/
    protected static final String SERVER_API_URL = "http://figz.dk/food/";
    private ServerAlertDialog serverAlertDialog;
    private String apiPath;
    private OnResponseListener<T> onResponseListener;

    /*Constructors*/
    public ServerComTask(String apiPath, ServerAlertDialog serverAlertDialog, OnResponseListener<T> onResponseListener, BasicNameValuePair ... basicNameValuePairs) {
        Log.w(TAG, "server query: " + ServerComTask.SERVER_API_URL + apiPath);
        this.apiPath = ServerComTask.SERVER_API_URL + apiPath;
        this.serverAlertDialog = serverAlertDialog;
        this.onResponseListener = onResponseListener;

        this.execute(basicNameValuePairs);
    }

    protected static String getImagePath(String fileName) {
        return ServerComTask.SERVER_API_URL + "upload/" + fileName;
    }

    private void showAlertDialog() {
        AlertDialog serverAlertDialog = this.serverAlertDialog.getServerAlertDialog();
        if (!serverAlertDialog.isShowing()) {
            serverAlertDialog.show();
        }
    }

    private void onFailed() {
        this.onResponseListener.onFailed();
        this.showAlertDialog();
    }

    protected abstract T parseJson(String json) throws Exception;

    /*Override methods*/
    @Override
    protected T doInBackground(BasicNameValuePair... pairs) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(Arrays.asList(pairs));
        //Create the http request
        HttpParams httpParams = new BasicHttpParams();

        //Setup timeouts
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
        HttpConnectionParams.setSoTimeout(httpParams, 15000);

        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost(this.apiPath);

        String response;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            response = EntityUtils.toString(entity);

        } catch (Exception e) {
            e.printStackTrace();
            response = null;
        }

        // if the communication failed, return null to the UI and let it handle the empty response
        if (response == null || response.equals("")) {
            return null;
        } else {
            try {
                Log.w(TAG, "Trying to parse JSON");
                return this.parseJson(response);
            } catch (Exception e) {
                Log.e(TAG, "Parsing JSON failed");
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(T result) {
        if (result == null) {
            this.onFailed();
            return;
        }

        // send the result
        this.onResponseListener.onResponse(result);
    }
}
