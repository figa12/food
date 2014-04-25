package aau.sw8.data;

import org.json.JSONArray;
import org.json.JSONObject;

import aau.sw8.model.User;

/**
 * Created by jacob on 4/25/14.
 */
public class UserCom extends ServerComTask<User> {

    public UserCom(ServerAlertDialog serverAlertDialog, OnResponseListener<User> onResponseListener) {
        super("user.php?", serverAlertDialog, onResponseListener);
    }

    @Override
    protected User parseJson(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        User user = null;

        if(json.length() == 1){
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            long userId = jsonObject.getLong("id");
            String username = jsonObject.getString("username");
            user = new User(userId, username, "");
        }else{
            //TODO: Error handling
            /*Error, there should not be more than one user returned*/
        }

        return user;
    }
}
