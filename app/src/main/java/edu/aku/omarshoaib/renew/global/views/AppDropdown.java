package edu.aku.omarshoaib.renew.global.views;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;

public class AppDropdown {

    /**
     * activity = activity reference
     * dropdown = dropdown
     * objList = list object or items
     * selectedItem = for edit mode i.e. if the dropdown is already selected
     * iDropdownCB = dropdown callback
     */

    public static void init(Activity activity, Spinner dropdown, Collection<?> objList, Object selectedItem, IDropdownCB iDropdownCB) {
        List<Object> _objList = new ArrayList<>(objList);
        _objList.add(0, activity.getString(R.string.please_select));
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(activity, R.layout.item_spinner, _objList);
        dropdown.setAdapter(adapter);

        // For Edit Mode
        if (!AppConstants.isEmpty(selectedItem))
            for (int i = 0; i < _objList.size(); i++) {
                if (_objList.get(i).toString().equals(selectedItem.toString())) {
                    dropdown.setSelection(i);
                    break;
                }
            }

        if (iDropdownCB != null) {
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0)
                        iDropdownCB.onItemSelected(dropdown, null, 0);
                    else
                        iDropdownCB.onItemSelected(dropdown, parent.getSelectedItem(), position - 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public interface IDropdownCB {
        void onItemSelected(Spinner spinner, Object object, int position);
    }

    /* IMPLEMENTATION CODE - FOR REFERENCE*/

    // STEP 1 - Override toString() in the object
//    @NonNull
//    @Override
//    public String toString() {
//        return fullName;
//    }

    // STEP 2 - Init list and pass it to AppDropdown.init()
//    List<User> userList = appDatabase.userDao().getAllData();

    // STEP 2.1 - In case if we need to show different property and select the different one
    // i.e. in this case we are showing full name of user in the dropdown list and
    // saving username in the object
//    User user = appDatabase.userDao().getUserByUsername(MainApp.formM1.getUsername());
//    AppDropdown.init(activity, bi.testDD, userList, user != null ? user.getFullName() : _EMPTY_, iDropdownCB);

    // STEP 3 - Dropdown selection callback listener
//    private AppDropdown.IDropdownCB iDropdownCB = (spinner, object, position) -> {
//        if (AppConstants.isEmpty(object))
//            MainApp.formM1.setUserCode(_EMPTY_);
//        else MainApp.formM1.setUsername(((User) object).getUsername());
//    };


}
