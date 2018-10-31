package com.androidboiz.whatsfordinner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Set;

/**
 * Class that creates and retrieves data from shared preference
 * The String with delimiters should look like this
 * String value = "name%directions%image%item1%count1%unit1%item2%%count2%unit2%..."
 *                           ^
 *            Should we add image location or url in the 2nd element?
 */
public class Storage {
    private SharedPreferences myPref;
    private SharedPreferences.Editor myEditor;

    public Storage(Context context) {
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        myEditor = myPref.edit();
    }

    /**
     * Enter a key and return the array of values
     * @param key recipe name
     * @return array of values
     */
    public String[] getValueArray(String key) {
        String attribute = myPref.getString(key, null);
        String[] arr;
        if (attribute != null) {
            arr = attribute.split("%");
        } else {
            arr = new String[0];
        }
        return arr;
    }

    /**
     * Gets the values from the key in unformatted value
     * @param key recipe name
     * @return String with delimiters
     */
    public String getValue(String key) {
        return myPref.getString(key, null);
    }

    /**
     * Store the value containing delimiters into the shared preference
     * @param key recipe name
     * @param value Strings with delimiters
     */
    public void setValue(String key, String value) {
        if (!value.isEmpty() && !key.isEmpty()) {
            myEditor.putString(key, value);
            myEditor.commit();
        }
    }

    /**
     * Enter an array of values along with the key and would store a string with delimiters onto shared preference
     * @param key recipe name
     * @param arr String array of values
     */
    public void setValueArray(String key, String[] arr) {
        String builder = "";
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].trim().isEmpty()) {
                builder = builder + arr[i] + "%";
            }
        }
        if (!builder.isEmpty()) {
            myEditor.putString(key, builder);
            myEditor.commit();
        }
    }

    /**
     * Return the size of the keys in the shared preference
     * @return number of keys
     */
    public int getKeySize() {
        Set<String> data = myPref.getAll().keySet();            //get all the key and value of the SharedPreference
        return data.size();
    }

    /**
     * Return an array of all the keys in the shared preference
     * @return array of keys
     */
    public String[] getAllKey() {
        return myPref.getAll().keySet().toArray(new String[getKeySize()]);
    }

    /**
     * Removes entry.  Should only be used for EditDish
     * @param key the key to remove
     * @return if successful
     */
    public boolean removeKey(String key) {
        if (!myPref.contains(key))
            return false;
        else {
            myEditor.remove(key);
            return true;
        }
    }
}