package github.mjksabit.akash.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

public interface RequestAction {
    public void handle(JSONObject param) throws Exception;
}
