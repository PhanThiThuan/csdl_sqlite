package com.example.csdl_sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database database;

    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    congviecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec = new ArrayList<>();

        adapter = new congviecAdapter(this, R.layout.dongcongviec, arrayCongViec);
        lvCongViec.setAdapter(adapter);
        //tạo database ghi chú
        database = new Database(this, "ghichu.sqlite",null,1);
        //Tạo bảng công việc
        database.QueryData("CREATE TABLE IF NOT EXITS CongViec(ID INT PRIMARY KEY OUTOINCREMENT, TENCV VARCHAR (200))");

        //them dữ liệu vào database
       // database.QueryData("INSERT INTO CongViec VALUES(null, 'Làm bài tập') ");
        GetDataCongViec();
    }
    private void GetDataCongViec (){
        //select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    private void DialogThem(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        final EditText edtTen = (EditText) dialog.findViewById(R.id.edittextTenCV);
        Button btnThem = (Button) dialog.findViewById(R.id.buttonthem);
        Button btnSua = (Button) dialog.findViewById(R.id.buttonHuy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tencv = edtTen.getText().toString();
                if (tencv.equals("")){
                    Toast.makeText(MainActivity.this,"Vui lòng nhập tên công việc",Toast.LENGTH_SHORT).show();
                }
                else {
                    database.QueryData("INSERT INTO CongViec VALUES(null, '"+tencv+"')");
                    Toast.makeText(MainActivity.this,"Đã Thêm",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}