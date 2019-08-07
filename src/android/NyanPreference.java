/** 
 * AppPreference -> NyanPreference
 * Get global android application preference
 * @Author
 * Source : Simon MacDonald<https://github.com/macdonst> 
 * ReCreate : Ryan Aunur Rassyid<https://github.com/nyancodeid>
 */
package cordova.plugin.preference;

import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * This class echoes a string called from JavaScript.
 */
public class NyanPreference extends CordovaPlugin {
    private static final String ERR_PROPERTY = "ERR_PROPERTY";
    private static final String ERR_COMMIT = "ERR_COMMIT";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
        
        if (action.equals("getAll")) {
            this.getAllPrefs(sharedPrefs, callbackContext);
        } else if (action.equals("get")) {
            String key = args.getString(0);
            this.getPrefs(key, sharedPrefs, callbackContext);
            return true;
        } else if (action.equals("set")) {
            String key = args.getString(0);
            String value = args.getString(1);
            this.setPrefs(key, value, sharedPrefs, callbackContext);
            return true;
        } else if (action.equals("remove")) {
            String key = args.getString(0);
            this.removePrefs(key, sharedPrefs, callbackContext);
            return true;
        } else if (action.equals("clear")) {
            this.clearPrefs(sharedPrefs, callbackContext);
            return true;
        }

        return false;
    }

    private void getAllPrefs(SharedPreferences sharedPrefs, CallbackContext callbackContext) {
        Map map = sharedPrefs.getAll();
		Iterator entries = map.entrySet().iterator();
		JSONObject response = new JSONObject();
		while(entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
		    String value = (String)entry.getValue();
		    
		    try {
				response.put(key, value);
			} catch (JSONException e) {
				continue;
			}
		}
	 	callbackContext.success(response);	
    } 
    private void getPrefs(String key, SharedPreferences sharedPrefs, CallbackContext callbackContext) {
        if (sharedPrefs.contains(key)) {
            Object obj = sharedPrefs.getAll().get(key);
            callbackContext.success(obj.toString());
        } else {
            _sendError(ERR_PROPERTY, "No such property called " + key, callbackContext);
        }
    }
    private void setPrefs(String key, String value, SharedPreferences sharedPrefs, CallbackContext callbackContext) {            
        Editor editor = sharedPrefs.edit();

        if ("true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase())) {
            editor.putBoolean(key, Boolean.parseBoolean(value));
        } else {
            editor.putString(key, value);
        }

        _commit(editor, callbackContext);
    }
    private void clearPrefs(SharedPreferences sharedPrefs, CallbackContext callbackContext) {
        Editor editor = sharedPrefs.edit();
        editor.clear();

        _commit(editor, callbackContext);
    }
    private void removePrefs(String key, SharedPreferences sharedPrefs, CallbackContext callbackContext) {
        if (sharedPrefs.contains(key)) {
            Editor editor = sharedPrefs.edit();
            editor.remove(key);
            
            _commit(editor, callbackContext);
        } else {
            _sendError(ERR_PROPERTY, "No such property called " + key, callbackContext);
        }
    }

    private void _commit(Editor editor, CallbackContext callbackContext) {
        try {
            editor.commit();
        } catch(Exception e) {
            _sendError(ERR_COMMIT, "Error while set key (" + e.getMessage() + ")", callbackContext);
        }
        callbackContext.success();
    }
    private void _sendError(String errorCode, String errorMessage, CallbackContext callbackContext) {
        JSONObject response = new JSONObject();
        try {
            response.put("error", errorCode);
            response.put("message", errorMessage);
        } catch(JSONException e) {
            callbackContext.error(e.getMessage());
        }
        callbackContext.error(response);
    }
}
