package amarnehsoft.com.debits.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import amarnehsoft.com.debits.controllers.SPController;
import amarnehsoft.com.debits.excell.PersonsExcel;

/**
 * Created by alaam on 10/5/2017.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new PersonsExcel(this)
//                .setFileName("p1")
//                .generate();

        boolean logged = SPController.newInstance(this).getLogged();
        if(logged)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
