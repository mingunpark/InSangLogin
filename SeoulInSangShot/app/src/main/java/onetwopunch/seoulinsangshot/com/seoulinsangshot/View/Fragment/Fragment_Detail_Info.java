package onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Constants;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.ViewCountVO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.Weather1VO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.Weather2VO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitClient;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitService;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Detail;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_ViewCount;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Weather1;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Weather2;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.DetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.net.URLDecoder.decode;

/**
 * Created by kwakgee on 2017. 9. 17..
 */

public class Fragment_Detail_Info extends Fragment {
    ImageView iv_map;
    ImageView iv_tip;
    ImageView iv_share;

    TextView areaTv;
    TextView areaEtv;
    TextView smarthPhoneTv;
    TextView appInfoTv;
    TextView transTv;
    TextView transTv2;

    TextView ddareungTv;
    TextView hitsTv;

    ImageView weather1;
    ImageView weather2;
    ImageView weather3;

    RelativeLayout firstRL;
    LinearLayout firstLL;
    FrameLayout secondFL;
    TextView secondTheme;
    TextView secondTip;
    TextView secondTemp;
    TextView secondTemp2;

    ArrayList<Model_Detail> detailMainList;
    String area;
    String initials;
    String name;
    String ename;
    String lat;
    String lng;
    String subway;
    String bus;
    String bicycle;
    String url;
    String smartPhone;
    String filter;
    String theme1;
    String theme2;
    String time;
    String tip;
    String info;

    String Date[];
    String baseDateArr[];
    String baseDate;
    String baseTimeArr[];
    String baseTime;

    String weather_PTY;
    String weather_SKY;

    ViewCountVO repo;
    ArrayList<Model_ViewCount> tempList;
    ArrayList<Model_ViewCount> viewList;

    int FABcounter = 0;
    Animation open, close;

    public Weather2VO repoList2;
    public Weather1VO repoList;
    public static ArrayList<Model_Weather1> weatherList1 = new ArrayList<Model_Weather1>();
    public static ArrayList<Model_Weather1> arr = new ArrayList<Model_Weather1>();
    public static ArrayList<Model_Weather2> weatherList2 = new ArrayList<Model_Weather2>();



    public Fragment_Detail_Info() {
    }

    public static Fragment_Detail_Info newInstance() {
        Fragment_Detail_Info fragment = new Fragment_Detail_Info();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FABcounter=0;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_info, container, false);

        tempList= new ArrayList<Model_ViewCount>();

        viewList = new ArrayList<Model_ViewCount>();

        smarthPhoneTv = (TextView) rootView.findViewById(R.id.smartphInfo);
        appInfoTv = (TextView) rootView.findViewById(R.id.appInfo);
        transTv = (TextView) rootView.findViewById(R.id.transportationInfo);
        transTv2 = (TextView) rootView.findViewById(R.id.transportationInfo2);

        ddareungTv = (TextView) rootView.findViewById(R.id.ddareungEInfo);
        areaTv = (TextView) rootView.findViewById(R.id.areaTextView);
        areaEtv = (TextView) rootView.findViewById(R.id.areaTextViewE);
        hitsTv = (TextView) rootView.findViewById(R.id.hitsTextView);


        firstRL = (RelativeLayout) rootView.findViewById(R.id.FirstRelative);
        firstLL = (LinearLayout) rootView.findViewById(R.id.ll_content);
        secondFL = (FrameLayout) rootView.findViewById(R.id.SecondFrame);

        secondTheme = (TextView) rootView.findViewById(R.id.secondTheme);
        secondTip = (TextView) rootView.findViewById(R.id.secondTip);
        secondTemp = (TextView) rootView.findViewById(R.id.secondInfo);

        weather1 = (ImageView) rootView.findViewById(R.id.weather1);
        weather2 = (ImageView) rootView.findViewById(R.id.weather2);
        weather3 = (ImageView) rootView.findViewById(R.id.weather3);

        open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);

        iv_map = (ImageView) rootView.findViewById(R.id.iv_map);
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + name + ")");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        iv_tip = (ImageView) rootView.findViewById(R.id.iv_tip);
        iv_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FABcounter++;
                firstRL.setVisibility(View.INVISIBLE);
                firstLL.setVisibility(View.INVISIBLE);
                secondFL.setVisibility(View.VISIBLE);
                secondFL.startAnimation(open);

                if (FABcounter == 2) {
                    firstRL.setVisibility(View.VISIBLE);
                    firstLL.setVisibility(View.VISIBLE);
                    secondFL.setVisibility(View.INVISIBLE);
                    secondFL.startAnimation(close);
                    FABcounter = 0;
                }
            }
        });

        final LinearLayout capView = DetailActivity.ll_capView;


        iv_share = (ImageView) rootView.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap b = Bitmap.createBitmap(capView.getWidth(), capView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(b);
                capView.draw(canvas);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), b, "Title", null);
                Uri imageUri = Uri.parse(path);
                getActivity().grantUriPermission("com.android.providers.media.MediaProvider", imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                getContext().startActivity(Intent.createChooser(share, "select"));

            }
        });

        area = getArguments().getString("area");
        initials = getArguments().getString("initials");
        name = getArguments().getString("name");
        ename = getArguments().getString("ename");
        lat = getArguments().getString("lat");
        lng = getArguments().getString("lng");
        subway = getArguments().getString("subway");
        bus = getArguments().getString("bus");
        bicycle = getArguments().getString("bicycle");
        url = getArguments().getString("url");
        smartPhone = getArguments().getString("smartPhone");
        filter = getArguments().getString("filter");
        theme1 = getArguments().getString("theme1");
        theme2 = getArguments().getString("theme2");
        tip = getArguments().getString("tip");
        info =getArguments().getString("info");

        areaTv.setText(name);
        areaEtv.setText(ename);
        smarthPhoneTv.setText(smartPhone);
        appInfoTv.setText(filter);
        transTv.setText(subway);
        transTv2.setText(bus);
        ddareungTv.setText(bicycle);
        secondTheme.setText(theme1 + " / " + theme2);
        secondTip.setText(tip);
        secondTemp.setText(info);

        setViewData("joker1649", url, initials);


        try {
            loadDataWeather1();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return rootView;

    }



    public void setViewData(String getid, String getUrl, String getInit) {
        final String id = "joker1649";
        final String url = getUrl;
        final String init = getInit;
        AndroidNetworking.post("http://13.124.87.34:3000/pview")
             .addBodyParameter("url", url)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*DetailActivity.viewList.add(new Model_ViewCount(init,id,text,date));
                        adapter_comment = new Adapter_Comment(getContext(), commentList);
                        rv_comment.setLayoutManager(manager);
                        rv_comment.setAdapter(adapter_comment);*/
                        loadViewData(init);

                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    public void loadViewData(final String getinit) {
        RetrofitService retrofitService = RetrofitClient.retrofit.create(RetrofitService.class);
        Call<ViewCountVO> call = retrofitService.getViewData();
        call.enqueue(new Callback<ViewCountVO>() {
            @Override
            public void onResponse(Call<ViewCountVO> call, Response<ViewCountVO> response) {
                repo = response.body();
                tempList = repo.getList();
                String init2 = getinit;
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getInit().equals(getinit)) {
                        String init=tempList.get(i).getInit();
                        String hit= tempList.get(i).getHit();

                        viewList.add(new Model_ViewCount(init, hit));

                    }

                }
                hitsTv.setText("Hits: " + viewList.get(0).getHit());

            }

            @Override
            public void onFailure(Call<ViewCountVO> call, Throwable t) {

            }
        });

    }

    public void loadDataWeather1() throws UnsupportedEncodingException {
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.reqURL_Weather).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        String nx = Constants.switchNX(area);
        String ny = Constants.switchNY(area);

        long now = System.currentTimeMillis();
        java.util.Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(date);
        String nowformat = sdfNow.format(nowCal.getTime());

        String nowDate[] = nowformat.split(" ");
        String nowTimeArr[] = nowDate[1].split(":");
        String nowTime = nowTimeArr[0] + nowTimeArr[1];
        int exceptionNow = Integer.parseInt(nowTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -40);
        String formatDate = sdfNow.format(cal.getTime());

        Date = formatDate.split(" ");
        baseDateArr = Date[0].split("/");
        baseTimeArr = Date[1].split(":");
        baseDate = baseDateArr[0] + baseDateArr[1] + baseDateArr[2];
        baseTime = baseTimeArr[0] + baseTimeArr[1];

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.add(Calendar.DATE, -1);
        String formatDate2 = sdfNow.format(cal2.getTime());
        String Date2[] = formatDate2.split(" ");
        String baseDateArr2[] = Date2[0].split("/");
        String baseTimeArr2[] = Date2[1].split(":");
        String baseDate2 = baseDateArr2[0] + baseDateArr2[1] + baseDateArr2[2];
        String baseTime2 = "2000";

        String serviceKey = decode("V46Xl9KDTxcFmWFQKiAsxp9XNgTkiYEbdmyBArTL5%2BIeh73QsZ%2BhPB2PS9QEx7bOhzZcaDDyqLLg1hrlD5bKEw%3D%3D", "UTF-8");

        if (exceptionNow != 2000) {

            int tempBaseTime = Integer.parseInt(baseTime);
            if ((tempBaseTime) > 1920) {
                baseDate2 = baseDate;
                baseTime2 = "0800";
            }
            Call<Weather2VO> call2 = service.loadWeather2(serviceKey, nx, ny, baseDate2, baseTime2, "300", "json");
            call2.enqueue(new Callback<Weather2VO>() {
                @Override
                public void onResponse(Call<Weather2VO> call, Response<Weather2VO> response) {
                    if (response.isSuccessful()) {
                        repoList2 = response.body();
                        weatherList2 = repoList2.getResponse().getBody().getItems().getItem();

                        long now = System.currentTimeMillis();
                        java.util.Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTime(date);
                        cal3.add(Calendar.DATE, 1);
                        String formatDate3 = sdfNow.format(cal3.getTime());
                        String Date3[] = formatDate3.split(" ");
                        String baseDateArr3[] = Date3[0].split("/");
                        String baseDate3 = baseDateArr3[0] + baseDateArr3[1] + baseDateArr3[2];

                        Calendar cal4 = Calendar.getInstance();
                        cal4.setTime(date);
                        cal4.add(Calendar.DATE, 2);
                        String formatDate4 = sdfNow.format(cal4.getTime());
                        String Date4[] = formatDate4.split(" ");
                        String baseDateArr4[] = Date4[0].split("/");
                        String baseDate4 = baseDateArr4[0] + baseDateArr4[1] + baseDateArr4[2];

                        String toSKYvalue = "";
                        String toPTYvalue = "";
                        String toSKYvalue2 = "";
                        String toPTYvalue2 = "";

                        for (int i = 0; i < weatherList2.size(); i++) {
                            if (weatherList2.get(i).getFcstDate().equals(baseDate3) &&
                                    weatherList2.get(i).getFcstTime().equals("0000") &&
                                    weatherList2.get(i).getCategory().equals("SKY")) {
                                toSKYvalue = weatherList2.get(i).getFcstValue();
                            }
                            else if (weatherList2.get(i).getFcstDate().equals(baseDate3) &&
                                    weatherList2.get(i).getFcstTime().equals("0000") &&
                                    weatherList2.get(i).getCategory().equals("PTY")) {
                                toPTYvalue = weatherList2.get(i).getFcstValue();
                            }
                            else if (weatherList2.get(i).getFcstDate().equals(baseDate4) &&
                                    weatherList2.get(i).getFcstTime().equals("0000") &&
                                    weatherList2.get(i).getCategory().equals("SKY")) {
                                toSKYvalue2 = weatherList2.get(i).getFcstValue();
                            }
                            else if (weatherList2.get(i).getFcstDate().equals(baseDate4) &&
                                    weatherList2.get(i).getFcstTime().equals("0000") &&
                                    weatherList2.get(i).getCategory().equals("PTY")) {
                                toPTYvalue2 = weatherList2.get(i).getFcstValue();
                            }
                        }

                        if (toPTYvalue.equals("1")) {
                            weather2.setImageResource(R.drawable.rain);
                        }
                        else if (toPTYvalue.equals("3") || toPTYvalue.equals("2")) {
                            weather2.setImageResource(R.drawable.snow);
                        }
                        else if ((toPTYvalue.equals("0")) && (toSKYvalue.equals("2") || toSKYvalue.equals("3") || toSKYvalue.equals("4"))) {
                            weather2.setImageResource(R.drawable.cloudy);
                        } else if (toSKYvalue.equals("1")) {
                            weather2.setImageResource(R.drawable.sunny);
                        } else {
                            weather2.setImageResource(R.drawable.nomal);
                        }

                        if (toPTYvalue2.equals("1")) {
                            weather3.setImageResource(R.drawable.rain);
                        }
                        else if (toPTYvalue2.equals("3") || toPTYvalue2.equals("2")) {
                            weather3.setImageResource(R.drawable.snow);
                        }
                        else if ((toPTYvalue2.equals("0")) && (toSKYvalue2.equals("2") || toSKYvalue2.equals("3") || toSKYvalue2.equals("4"))) {
                            weather3.setImageResource(R.drawable.cloudy);
                        } else if (toSKYvalue2.equals("1")) {
                            weather3.setImageResource(R.drawable.sunny);
                        } else {
                            weather3.setImageResource(R.drawable.nomal);
                        }


                    }
                }

                @Override
                public void onFailure(Call<Weather2VO> call, Throwable t) {
                    Log.v("brokenError", "error");
                }
            });

            Call<Weather1VO> call = service.loadWeather1(serviceKey, nx, ny, baseDate, baseTime, "json");
            call.enqueue(new Callback<Weather1VO>() {
                @Override
                public void onResponse(Call<Weather1VO> call, Response<Weather1VO> response) {
                    if (response.isSuccessful()) {
                        repoList = response.body();
                        weatherList1 = repoList.getResponse().getBody().getItems().getItem();

                        for (int i = 0; i < weatherList1.size(); i++) {
                            String baseDate = weatherList1.get(i).getBaseDate();

                            String baseTime = weatherList1.get(i).getBaseTime();
                            String category = weatherList1.get(i).getCategory();
                            String nx = weatherList1.get(i).getNx();
                            String ny = weatherList1.get(i).getNy();
                            String obsrValue = weatherList1.get(i).getObsrValue();
                            if (category.equals("PTY")) {
                                arr.add(0, new Model_Weather1(baseDate, baseTime, category, nx, ny, obsrValue));
                            }
                            if (category.equals("SKY")) {
                                arr.add(1, new Model_Weather1(baseDate, baseTime, category, nx, ny, obsrValue));
                            }
                        }
                        switch (arr.get(0).getObsrValue())
                        {
                            case "0":
                                weather_PTY = "nomal";
                                break;
                            case "1":
                                weather_PTY = "rain";
                                break;
                            case "2":
                                weather_PTY = "rain_snow";
                                break;
                            case "3":
                                weather_PTY = "snow";
                        }
                        switch (arr.get(1).getObsrValue())
                        {
                            case "1":
                                weather_SKY = "sunny";
                                break;
                            case "2":
                                weather_SKY = "little_cloudy";
                                break;
                            case "3":
                                weather_SKY = "cloudy";
                                break;
                            case "4":
                                weather_SKY = "gray";
                        }

                        if (weather_PTY.equals("rain")) {
                            weather1.setImageResource(R.drawable.rain);
                        }
                        else if (weather_PTY.equals("snow") || weather_PTY.equals("rain_snow")) {
                            weather1.setImageResource(R.drawable.snow);
                        }
                        else if ((weather_PTY.equals("nomal")) && (weather_SKY.equals("lttle_cloudy") || weather_SKY.equals("cloudy") || weather_SKY.equals("gray"))) {
                            weather1.setImageResource(R.drawable.cloudy);
                        } else if (weather_SKY.equals("sunny")) {
                            weather1.setImageResource(R.drawable.sunny);
                        } else {
                            weather1.setImageResource(R.drawable.nomal);
                        }

                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<Weather1VO> call, Throwable t) {
                    Log.d("MainActivity", "error loading from API");
                }
            });
        }
    }
}