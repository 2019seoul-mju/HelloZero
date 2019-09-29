package com.example.zeropayfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchMap extends FragmentActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManger;
    private Marker currentMarker = null;
    public Button btnSearchLocation;
    private String Location_result;
    private EditText Location_name;
    private RecyclerAdapter adapter;

    private static final String TAG = "googlemap_search";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소


    Location mCurrentLocatiion;
    LatLng currentPosition;
    InputMethodManager imm;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;
    public String jwt2;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_searchmap);
        mLayout = findViewById(R.id.Searchmap);

        Location_name = (EditText) findViewById(R.id.LocationText);
        intent=new Intent(this.getIntent());
        final String jwt=intent.getStringExtra("jwt");
        jwt2 = jwt;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Searchmap);
        mapFragment.getMapAsync(this);
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        final GestureDetector gestureDetector = new GestureDetector(SearchMap.this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child!=null&&gestureDetector.onTouchEvent(e)) {
                    TextView tv = (TextView) rv.getChildViewHolder(child).itemView.findViewById(R.id.textView3);

                    String[] word =  tv.getText().toString().split(",");
                    LatLng currentLatLng = new LatLng(Double.parseDouble(word[0]), Double.parseDouble(word[1]));

                    setCurrentLocation(currentLatLng);

                    Log.d(TAG, "" + rv.getChildViewHolder(child).itemView.findViewById(R.id.textView3));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;


        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(SearchMap.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }

        btnSearchLocation = (Button) findViewById(R.id.search_button);

        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectServer_get(Location_name.getText().toString());
                imm.hideSoftInputFromWindow(Location_name.getWindowToken(), 0);
                init();
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                String[] word = marker.getTitle().split(",");
                mapdetail.setFranchise_no(Integer.parseInt(word[2]));
                mapdetail.setFranchise_name(word[0]);
                return false;
            }
        });

        mClusterManger = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManger);
        mMap.setOnMarkerClickListener(mClusterManger);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                intent = new Intent(getApplicationContext(),mapdetail.class);
                intent.putExtra("jwt",jwt2);
                startActivity(intent);//액티비티 띄우기
            }
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            Marker marker;
            boolean isShowWindow = false;

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                this.marker = arg0;
                View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                try {

                    // Getting view from the layout file info_window_layout
                    v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                    // Getting reference to the TextView to set latitude
                    String[] word = arg0.getTitle().split(",");
                    ImageView Marker_Img = (ImageView) v.findViewById(R.id.Marker_Img);
                    String imageUrl = word[1].replace( "\\", "");
                    Glide.with(SearchMap.this).load(imageUrl).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (isShowWindow == false) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 현재 UI 스레드가 아니기 때문에 메시지 큐에 Runnable을 등록 함
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                // 메시지 큐에 저장될 메시지의 내용
                                                marker.showInfoWindow();
                                            }
                                        });
                                    }
                                }).start();
                                isShowWindow = true;
                            } else {
                                isShowWindow = false;
                            }
                            return false;
                        }
                    }).into(Marker_Img);
                    TextView Marker_Title = (TextView) v.findViewById(R.id.Marker_Title);
                    Marker_Title.setText(word[0]);
                    TextView Marker_Snippet = (TextView) v.findViewById(R.id.Marker_Snippet);
                    Marker_Snippet.setText(arg0.getSnippet());
                    mapdetail.setFranchise_no(Integer.parseInt(word[2]));
                    mapdetail.setFranchise_name(word[0]);
                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }

                return v;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                return null;
            }
        });

//        addItems();
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }


    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchMap.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

    public void Addmarker(String Location){
            mMap.clear();
            Gson gson = new Gson();
            SearchMap.LocationGson info = gson.fromJson(Location_result, SearchMap.LocationGson.class);
            if(info.result.size() != 0) {
                for (int i = 0; i < info.result.size(); i++) {
                    if (info.result.get(i).Franchise_Long != null && info.result.get(i).Franchise_Lat != null) {
                        LatLng LatLng = new LatLng(Double.parseDouble(info.result.get(i).Franchise_Long), Double.parseDouble(info.result.get(i).Franchise_Lat));
                        String markerSnippet = "도로명 주소:" + String.valueOf(info.result.get(i).Franchise_RoadLoc) + "\n"
                                + "주소 :" + String.valueOf(info.result.get(i).Franchise_State) + " " + String.valueOf(info.result.get(i).Franchise_Loc) + "\n"
                                + "소개 :" + String.valueOf(info.result.get(i).Franchise_Desc);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(LatLng);
                        markerOptions.title(info.result.get(i).Franchise_Name + "," + info.result.get(i).Franchise_Uri + "," + info.result.get(i).Franchise_No);
                        markerOptions.snippet(markerSnippet);
                        markerOptions.draggable(true);
                        mMap.addMarker(markerOptions);
                        MyItem offsetItem = new MyItem(Double.parseDouble(info.result.get(i).Franchise_Long), Double.parseDouble(info.result.get(i).Franchise_Lat));
                        mClusterManger.addItem(offsetItem);
                        Data data = new Data();
                        data.setTitle(info.result.get(i).Franchise_Name);
                        data.setContent(markerSnippet);
                        data.setLatlng(info.result.get(i).Franchise_Long+ "," +info.result.get(i).Franchise_Lat);
                        data.setResId(info.result.get(i).Franchise_Uri.replace( "\\", ""));
                        adapter.addItem(data);

                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getApplicationContext(), "가맹점이 없습니다.", Toast.LENGTH_SHORT).show();
            }
    }
    public void ConnectServer_get(String search){
        SearchMap.ConnectServer connectServerGet = new SearchMap.ConnectServer();
        try {
            URLEncoder.encode("http://15.164.118.95/hello/searchZero/"+ search,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connectServerGet.requestGet("http://15.164.118.95/hello/searchZero/"+ search, "location");
    }

    public void setCurrentLocation(LatLng currentLatLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
    }

    public class LocationGson {
        List<Location_info> result;
    }

    public class Location_info {
        int Franchise_No;
        String Franchise_RoadLoc;
        String Franchise_Loc;
        String Franchise_State;
        String Franchise_Name;
        String Franchise_Desc;
        String Franchise_Lat;
        String Franchise_Long;
        String Franchise_Uri;
    }

    class ConnectServer {

        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestGet(String url, String searchKey) {
            //URL에 포함할 Query문 작성 Name&Value
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("searchKey", searchKey);
            String requestUrl = urlBuilder.build().toString();

            //Query문이 들어간 URL을 토대로 Request 생성
            Request request = new Request.Builder().url(requestUrl).build();

            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {

                    Location_result = response.body().string();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                // 현재 UI 스레드가 아니기 때문에 메시지 큐에 Runnable을 등록 함
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                    // 메시지 큐에 저장될 메시지의 내용
                                        Addmarker(Location_result);
                                    }
                                });
                        }
                    }).start();
                }
            });
        }
    }
}
