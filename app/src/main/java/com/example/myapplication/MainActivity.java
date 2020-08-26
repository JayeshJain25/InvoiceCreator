package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
            , HomeScreen.onFragmentBtnSelected, HomeScreen.onFragmentBtnClicked, PDFData.savePDFInterface, PdfList.viewListPDF{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bitmap bmp1,bmp2,scaledBitmap2, scaledBitmap1;
    File folder;
    String[] items;
    public static ArrayList<File> mFiles =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new HomeScreen());
        fragmentTransaction.commit();

        bmp1 =  BitmapFactory.decodeResource(getResources(),R.drawable.headerpart);
        scaledBitmap1 = Bitmap.createScaledBitmap(bmp1,800,122,false);

        bmp2 = BitmapFactory.decodeResource(getResources(),R.drawable.footerpart);
        scaledBitmap2 = Bitmap.createScaledBitmap(bmp2,300,100,false);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new HomeScreen());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.listPDF){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new PdfList());
            fragmentTransaction.commit();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.create();
        alert.setTitle("Confirmation!!");
        alert.setMessage("Do you want to exit ?");
        alert.setCancelable(false);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alert.show();

    }

    @Override
    public void onButtonSelected() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment,new PDFData());
        fragmentTransaction.commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            savePDF();

        }else{

            Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show();
        }
    }
    //method to save the pdf
    private void savePDF() {
        //Creating the pdf document object
        PdfDocument myPdfDocument = new PdfDocument();
        //Creating the paint object
        Paint paint = new Paint();

        //creating the page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(830,1000,1).create();
        PdfDocument.Page page = myPdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledBitmap1,0,0,paint);
        canvas.drawBitmap(scaledBitmap2,10,900,paint);
        myPdfDocument.finishPage(page);
        //generating the file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //File file = new File(getExternalFilesDir(null),mFileName+".pdf");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),mFileName+".pdf");
        File docFile = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
        if(!docFile.exists()){
            docFile.mkdirs();
        }else {


      /*  String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
       Intent intent =   new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE,mFileName+".pdf");

        startActivityForResult(intent,1); */


            try {
                //writing to the location
                myPdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "Saved Successfully " + file.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //closing the pdf document object
        myPdfDocument.close();
    }

    @Override
    public void onButtonClicked() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment,new PdfList());
        fragmentTransaction.commit();
    }

    @Override
    public void savePDFIn() {
        //checking for runtime permission
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //if permission not granted then request for permission
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);

                }else{//calling savePDF method

                    savePDF();

                }
            }else{
                //calling savePDF method
                savePDF();

            }
    }
    @Override
    public void listPDF(RecyclerView recyclerView) {

        //checking for runtime permission
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //if permission not granted then request for permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);

            }else{
                //calling savePDF method
                initViews(recyclerView);

            }
        }else{
            //calling savePDF method
            initViews(recyclerView);

        }

    }

    private void initViews(RecyclerView recyclerView){

        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        mFiles = getPdfFiles(folder);

        ArrayList<File> myPDF = getPdfFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        items = new String[myPDF.size()];
        for (int i=0 ; i<items.length;i++){

            items[i] = myPDF.get(i).getName().replace(".pdf","");
        }

        PDFAdapter pdfAdapter = new PDFAdapter(this, mFiles,items);
        recyclerView.setAdapter(pdfAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

    }

    private ArrayList<File> getPdfFiles(File folder) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = folder.listFiles();
        if(files != null){
            for(File singleFile : files){
                if(singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(getPdfFiles(singleFile));
                }else{

                    if(singleFile.getName().endsWith(".pdf") && singleFile.getName().replace(".pdf","").matches("^[0-9]+_[0-9]+$")){
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                assert data != null;
                Uri uri = data.getData();

                try {
                    //Creating the pdf document object
                    PdfDocument myPdfDocument = new PdfDocument();
                    //Creating the paint object
                    Paint paint = new Paint();

                    //creating the page
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(830,1000,1).create();
                    PdfDocument.Page page = myPdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    canvas.drawBitmap(scaledBitmap1,0,0,paint);
                    canvas.drawBitmap(scaledBitmap2,10,900,paint);
                    myPdfDocument.finishPage(page);

                    assert uri != null;
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);

                    assert outputStream != null;
                  myPdfDocument.writeTo(outputStream);
                    Toast.makeText(this,"Saved Successfully " + outputStream.toString(),Toast.LENGTH_SHORT).show();
                    outputStream.close();
                    myPdfDocument.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } */
}
