package in.idappadi.idappadimanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class NewBusiness extends Activity {
    EditText businessName;
    EditText businessAddress;
    EditText businessCity;
    EditText businessState;
    EditText businessCountry;
    EditText businessPhone;
    EditText businessContact;
    EditText businessCatalog;
    String surveyorPhone;
    DBController controller = new DBController(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_user);
        businessName = (EditText) findViewById(R.id.businessname);
        businessAddress = (EditText) findViewById(R.id.businessaddress);
        businessCity = (EditText) findViewById(R.id.businesscity);
        businessState = (EditText) findViewById(R.id.businessstate);
        businessCountry = (EditText) findViewById(R.id.businesscountry);
        businessPhone = (EditText) findViewById(R.id.businessphone);
        businessContact = (EditText) findViewById(R.id.businesscontact);
        businessCatalog = (EditText) findViewById(R.id.businesscatalog);

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        surveyorPhone=mPhoneNumber;
    }

    /**
     * Called when Save button is clicked
     * @param view
     */
    public void addNewBusiness(View view) {
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("businessname", businessName.getText().toString());
        queryValues.put("businessaddress", businessAddress.getText().toString());
        queryValues.put("businesscity", businessCity.getText().toString());
        queryValues.put("businessstate", businessState.getText().toString());
        queryValues.put("businesscountry", businessCountry.getText().toString());
        queryValues.put("businessphone", businessPhone.getText().toString());
        queryValues.put("businesscontact", businessContact.getText().toString());
        queryValues.put("businesscatalog", businessCatalog.getText().toString());
        queryValues.put("surveyorphone", surveyorPhone);

        if (businessName.getText().toString() != null
                && businessName.getText().toString().trim().length() != 0) {
            controller.insertBusiness(queryValues);
            this.callHomeActivity(view);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter User name",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navigate to Home Screen
     * @param view
     */
    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(objIntent);
    }

    /**
     * Called when Cancel button is clicked
     * @param view
     */
    public void cancelAddBusiness(View view) {
        this.callHomeActivity(view);
    }
}
