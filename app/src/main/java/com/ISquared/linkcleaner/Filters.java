package com.ISquared.linkcleaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Objects;
import java.util.Scanner;

import static com.ISquared.linkcleaner.Constants.FILTER;
import static com.ISquared.linkcleaner.Constants.THEME;

public class Filters extends AppCompatActivity {
    final boolean[] changes = {false}, saved = {true};
    EditText customFilters;
    StringBuffer finalFilters;
    String TAG = "Filters", setText;
    TextWatcher textWatcher;
    Button saveButton, discardButton;

    @Override
    protected void onPause() {
        super.onPause();
        customFilters.removeTextChangedListener(textWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        customFilters.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Theme theme = new Theme();
        int themeValue = theme.returnTheme(prefs.getInt(THEME, 0), getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
        setTheme(themeValue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.custom_filter_header));
        final SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getString(FILTER, "invalid").equals("invalid"))
            editor.putString(FILTER, Constants.FILTERS).apply();

        setText = prefs.getString(FILTER, "invalid");
        finalFilters = new StringBuffer();
        saveButton = findViewById(R.id.saveButton);
        discardButton = findViewById(R.id.discardButton);
        customFilters = findViewById(R.id.customFilters);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, charSequence + "");
                if (!customFilters.getText().toString().trim().equals(setText.trim()) &&
                        getCurrentFocus() == customFilters) {
                    setText = customFilters.getText().toString().trim();
                    changes[0] = true;
                    saved[0] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        customFilters.addTextChangedListener(textWatcher);
        if (!setText.equals("invalid")) {
            customFilters.setText(setText.trim());
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filters = customFilters.getText().toString();
                Scanner scanner = new Scanner(filters);
                while (scanner.hasNextLine()) {
                    String testFilter = scanner.nextLine();
                    String[] filter = testFilter.split(",");
                    if (filter.length == 4 && filter[0].equalsIgnoreCase("split")) {
                        int index = -99;
                        try {
                            index = Integer.parseInt(filter[3]);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Something is wrong... "
                                    + filter[3] + " is not a number.", Toast.LENGTH_LONG).show();
                        }
                        if (index > -1) {
                            saveFilters(testFilter);
                        } else
                            Toast.makeText(getApplicationContext(), "index cannot be negative.",
                                    Toast.LENGTH_LONG).show();

                    } else if ((filter.length == 4 || filter.length == 3) && filter[0].equalsIgnoreCase("replace")) {
                        saveFilters(testFilter);
                    } else if (filter.length == 3 && (filter[0].equalsIgnoreCase("append") ||
                            filter[0].equalsIgnoreCase("prepend"))) {
                        saveFilters(testFilter);
                    } else {
                        Toast.makeText(getApplicationContext(), "uh-oh", Toast.LENGTH_SHORT).show();
                    }
                }
                if (changes[0]) {
                    editor.putString(FILTER, finalFilters.toString());
                    editor.apply();
                    saved[0] = true;
                    customFilters.setText(finalFilters.toString().trim());
                    //customFilters.clearFocus();
                    changes[0] = false;
                }
            }
        });
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved[0] = false;
                customFilters.setText(Constants.FILTERS);
            }
        });
    }

    private void saveFilters(String testFilter) {
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        saved[0] = true;
        changes[0] = true;
        if (!finalFilters.toString().contains(testFilter))
            finalFilters.append(testFilter).append("\n");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!saved[0]) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.back_button_title))
                        .setMessage(getString(R.string.back_button_message))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Filters.this, SettingsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Intent intent = new Intent(Filters.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Filters.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
