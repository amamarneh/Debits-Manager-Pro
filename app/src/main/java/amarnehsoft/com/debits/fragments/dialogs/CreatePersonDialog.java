package amarnehsoft.com.debits.fragments.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.utils.Defualts;

/**
 * Created by jcc on 12/3/2017.
 */

public abstract class CreatePersonDialog extends AlertDialog.Builder{

    public CreatePersonDialog(@NonNull final Context context, final String personName) {
        super(context);

        setTitle(context.getString(R.string.unknounPerson))
                .setMessage(context.getString(R.string.thisPersonDoesntExistAddIt))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Person person = new Person();
                        person.setKey(UUID.randomUUID().toString());
                        person.setName(personName.trim());
                        person.setPhone("");
                        person.setEmail("");
                        person.setCatCode(Defualts.DEFUALT);
                        person.setIsDeleted(0);

                        PersonsDB.getInstance(context).saveBean(person);

                        savedSuccessfully(person);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public abstract void savedSuccessfully(Person person);
}
