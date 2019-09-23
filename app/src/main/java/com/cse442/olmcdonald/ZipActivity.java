package com.cse442.olmcdonald;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Activity for zip input dialog to display sellers nearby
 *
 * @Author Kevin
 */
public class ZipActivity extends Dialog {

  TextView txt_dia;
  Button but_check;
  EditText et_zip;
  LinearLayout linearLayout;
  public ZipActivity(@NonNull Context context) {
    super(context);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_zip);
    this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    but_check = findViewById(R.id.btn_check);
    et_zip = findViewById(R.id.et_zip);
    linearLayout = findViewById(R.id.ll_zip);
    txt_dia = findViewById(R.id.txt_dia);
    but_check.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(et_zip.getText().toString().equals("14228")){
          linearLayout.removeView(et_zip);
          txt_dia.setText("There are 10 sellers nearby...");
          but_check.setText("Done");
          but_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dismiss();
            }
          });

        }
      }
    });
    /*Hard code zip code*/
  }
}