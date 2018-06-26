package com.example.shubhang.ecommerce;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.shubhang.ecommerce.adapter.ProductListThumbnailAdapter;
import com.example.shubhang.ecommerce.model.ProductList;
import com.example.shubhang.ecommerce.model.products;
import com.example.shubhang.ecommerce.network.GetDataService;
import com.example.shubhang.ecommerce.network.RetrofitClientInstance;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ProductListThumbnailAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<products> pList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching Products for you ....");
        progressDialog.show();
        getAns();
    }

    private void addListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Intent intent = new Intent(MainActivity.this,ItemDescription.class);
                products p = pList.get(position);
                intent.putExtra("Name",p.getName());
                intent.putExtra("Description",p.getDescription());
                intent.putExtra("Brand",p.getBrand());
                intent.putExtra("Price",p.getPrice());
                intent.putExtra("Images",p.getImages());
                intent.putExtra("Currency",p.getCurrency());
                intent.putExtra("inStock",p.isIn_stock());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));
    }

    @SuppressLint("StaticFieldLeak")
    public void getAns(){

        Log.d("Execute Query","Happening");
        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                String token = "415e4908-fa3f-4a27-91ac-908bf40f2654";

                SharedPreferences sPref = getSharedPreferences("myFilters", Context.MODE_PRIVATE);
                String category = sPref.getString("category", "");
                String price = sPref.getString("price", "");
                String brand = sPref.getString("brand","");
                String name = sPref.getString("name","");
                category = category == "" ? "" : " category:" + category + " ";
                price = price == "" ? "" : " price:" + price + " ";
                brand = brand == "" ? "" : " brand:" + brand + " ";
                name = name == "" ? "" : " name:"+name+" ";
                String Query = name+category+brand+price;
                Log.e("QueryString", Query);
                Call<ProductList> call = service.getProducts(token, "json", Query);
                call.enqueue(new Callback<ProductList>() {

                    @Override
                    public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                       progressDialog.dismiss();
                        try {
                            pList = response.body().products;
                            generateDataList(pList);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Could Not Fetch Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductList> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("TAG1", t.toString());
                        Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

                return null;
            }
        }.execute();


    }

    public void setupView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_filter) {
           Intent filter_intent = new Intent(MainActivity.this,FilterActivity.class);
           startActivity(filter_intent);
        }
        else if (id == R.id.nav_outlook) {
            mailTo("skssinghal@outlook.com");
        } else if (id == R.id.nav_fb) {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        }
        else if(id == R.id.nav_gmail){
            mailTo("skssinghal@gmail.com");
        }
        else if(id == R.id.nav_instagram){

        }
        else if(id == R.id.action_logout){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this, FilterActivity.class));
                            finish();
                        }
                    });
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mailTo(String email){
        String mailto = "mailto:"+email+
                "?cc=" + "singhalshalu1971@gmail.com" +
                "&subject=" + Uri.encode("Regarding Product Information");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/pages/Electro-Comp-Total-Services/1405045876221135";
    public static String FACEBOOK_PAGE_ID = "pages/Electro-Comp-Total-Services/1405045876221135";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void generateDataList(List<products> productList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new ProductListThumbnailAdapter(productList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addListeners();
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
