package com.example.quizapp;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadQestion extends AsyncTask<String,Integer,String[]> {

    FragmentActivity activity;
    public DownloadQestion(FragmentActivity activity)
    {
        this.activity=activity;
    }

    protected String[] doInBackground(String... urlIn) {
        URL url;
        HttpURLConnection urlConnection;
        String result = "";
        String question[]=new String[15];
        try {
            url = new URL(urlIn[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int d = reader.read();
            result = "";
            while (d != -1) {
                char ch = (char) d;
                result = result + ch;
                d = reader.read();
            }
            JSONObject ob=new JSONObject(result);
            String quest=ob.getString("results");
            JSONArray arr=new JSONArray(quest);
            for(int x=0;x<15;x++)
            {
                JSONObject object=new JSONObject(arr.getString(x));
                String q=object.getString("question");
                String ans=object.getString("correct_answer");
                String opt=object.getString("incorrect_answers");
                int r=(int)Math.floor(Math.random()*(4-1+1)+1);
                JSONArray ar=new JSONArray(opt);
                JSONArray options=new JSONArray();
                int t=0;
                for(int i=1;i<=4;i++)
                {
                    if(i==r)
                    {
                        options.put(i-1,ans);
                    }
                    else
                    {
                        options.put(i-1,ar.get(t));
                        t++;
                    }
                }
                JSONObject queAns=new JSONObject();
                queAns.put("Question",q);
                queAns.put("Answer",ans);
                queAns.put("Options",options.toString());
                question[x]=queAns.toString();
            }

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        return question;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        Singleton singleton=Singleton.getInstance();
        singleton.setQuestions(result);
        FragmentManager fragmentManager=activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragmentContainer,QuestionFragment.class,null).commit();
    }
}
