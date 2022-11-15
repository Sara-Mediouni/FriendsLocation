package com.mobile.freindslocation.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.freindslocation.JSONParser;
import com.mobile.freindslocation.MyLocation;
import com.mobile.freindslocation.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<MyLocation> data=new ArrayList<MyLocation>();
    ArrayAdapter ad;
    MyLocation ml;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ad = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);
        binding.lvlocations.setAdapter(ad);
        binding.idBtnDownload.setOnClickListener(view -> {
            Telechargement t = new Telechargement(getActivity());
            t.execute();
        });

        binding.lvlocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ml=data.get(i);
            }
        });


        return root;

    }

    class Telechargement extends AsyncTask{
        Context con;
        AlertDialog alert;

        public Telechargement(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Telechargement");
            builder.setMessage("Veuillez patientez...");
            alert=builder.create();
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //second thread
            String ip="192.168.5.88";
            String url="http://"+ip+"/servicephp/getAll.php";
            JSONParser parser=new JSONParser();
            JSONObject response = parser.makeHttpRequest(url,"GET",null);
            try {
                int success = response.getInt("success");
                if (success==0){
                    String msg=response.getString("message");
                }
                else {
                    JSONArray tableau = response.getJSONArray("Ami");
                    for (int i=0;i<tableau.length();i++){
                        JSONObject ligne = tableau.getJSONObject(i);
                        String nom = ligne.getString("nom");
                        String numero = ligne.getString("numero");
                        String longitude = ligne.getString("longitude");
                        String latitude = ligne.getString("latitude");
                        data.add(new MyLocation(nom,numero,longitude,latitude));

                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ad.notifyDataSetChanged();
            alert.dismiss();
        }
    }

    class Delete extends AsyncTask{
        Context con;
        AlertDialog alert;

        public Delete(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Suppression");
            builder.setMessage("Veuillez patientez...");
            alert=builder.create();
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //second thread
            String ip="192.168.5.88";
            String url="http://"+ip+"/servicephp/delete.php";
            JSONParser parser=new JSONParser();
            JSONObject response = parser.makeHttpRequest(url,"GET",null);
            try {
                int success = response.getInt("success");
                if (success==0){
                    String msg=response.getString("message");
                }
                else {
                    JSONArray tableau = response.getJSONArray("Ami");
                    for (int i=0;i<tableau.length();i++){
                        JSONObject ligne = tableau.getJSONObject(i);
                        String nom = ligne.getString("nom");
                        String numero = ligne.getString("numero");
                        String longitude = ligne.getString("longitude");
                        String latitude = ligne.getString("latitude");
                        data.add(new MyLocation(nom,numero,longitude,latitude));

                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ad.notifyDataSetChanged();
            alert.dismiss();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}