package aau.sw8.data;

import org.json.JSONObject;

import aau.sw8.model.User;

/**
 * Created by jacob on 4/25/14.
 */
public class UserCom extends ServerComTask<String> {
    private String hash;

    public UserCom(ServerAlertDialog serverAlertDialog, OnResponseListener<String> onResponseListener, String hash) {
        super(UserCom.getPath(hash), serverAlertDialog, onResponseListener);
        this.hash = hash;
    }

    private static String getPath(String hash){
        return "user.php?hash=" + hash;
    }

    @Override
    protected String parseJson(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getString("hash");
    }
}
