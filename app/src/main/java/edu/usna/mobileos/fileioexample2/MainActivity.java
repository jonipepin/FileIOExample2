package edu.usna.mobileos.fileioexample2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static String LOG_TAG = "PEPIN";

    String myFileName = "exampleListObject";

    EditText mEdittext;
    Button mAddButton, mClearButton;
    ListView mListview;
    ArrayAdapter<String> mArrayAdapter;

    List<String> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create all View objects
        mEdittext = (EditText) findViewById(R.id.myEditText);
        mAddButton = (Button) findViewById(R.id.myAddButton);
        mClearButton = (Button) findViewById(R.id.myClearButton);
        mListview = (ListView) findViewById(R.id.myListView);
    }

    @Override
    public void onStart() {
        super.onStart();

        // get Object from file
        Object obj = getObjectFromFile(this, myFileName);

        // if obj returns something, check it's type
        if (obj != null && obj instanceof ArrayList) {
            // use the existing List id it exists
            myList = (ArrayList<String>) obj;
        } else {
            // create new List if one doesn't exist
            myList = new ArrayList<String>();
        }

        // create array adapter for populating ListView
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        mListview.setAdapter(mArrayAdapter);

        // add 'Add' Button onclick listener
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get string from edittext
                String enteredString = mEdittext.getText().toString();

                // check to make sure the string is not empty
                if (!enteredString.isEmpty()) {

                    // add the string to the list
                    myList.add(enteredString);

                    // reset the EditText
                    mEdittext.setText("");

                    // update the ListView display
                    mArrayAdapter.notifyDataSetChanged();

                }

            }
        });

        // add 'Clear' Button onclick listener
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear the list contents
                myList.clear();

                // update the ListView display
                mArrayAdapter.notifyDataSetChanged();

            }

        });

    }

    @Override
    public void onStop() {
        super.onStop();

        // save the list object to file
        saveObjectToFile(this, myFileName, myList);
    }

    public static void saveObjectToFile(Context context, String fileName, Object obj) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "saveObjectToFile FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "saveObjectToFile IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, "saveObjectToFile Exception: " + e.getMessage());
        }
    }

    public static Object getObjectFromFile(Context context, String filename) {

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object object = ois.readObject();
            ois.close();

            return object;

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile FileNotFoundException: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "getObjectFromFile IOException: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile ClassNotFoundException: " + e.getMessage());
            return null;
        } catch (Exception e) {// Catch exception if any
            Log.e(LOG_TAG, "getBookmarksFromFile Exception: " + e.getMessage());
            return null;
        }
    }

}
