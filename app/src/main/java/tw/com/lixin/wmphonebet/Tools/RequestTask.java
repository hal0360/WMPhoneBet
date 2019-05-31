package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.com.atromoby.utils.CmdRes;
import tw.com.atromoby.utils.Response;


public class RequestTask extends AsyncTask<String,String,Response>
{
    private String aus;
    private String url;
    private CmdRes cmdResSuc, cmdResErr, cmdResFail;

    public RequestTask(Context context, String url, String au) {
        this.url = url;
        aus = au;
    }

    public void onSuccess(CmdRes cmd){
        cmdResSuc = cmd;
    }

    public void onError(CmdRes cmd){
        cmdResErr = cmd;
    }

    public void onFail(CmdRes cmd){
        cmdResFail = cmd;
    }

    public void send(String data){
            execute(data);
    }

    public void fetch(){
        String data = null;
        execute(data);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response doInBackground(String... params) {
        return myHttpConnection(params[0], url, aus);
    }

    @Override
    protected void onPostExecute(Response response) {
        if(response.statusCode >= 200 && response.statusCode < 300){
            if(cmdResSuc != null) cmdResSuc.exec(response);
        }else if(response.statusCode >= 400 && response.statusCode < 500){
            if(cmdResErr != null) cmdResErr.exec(response);
        }else {
            if(cmdResFail != null) cmdResFail.exec(response);
        }
    }

    public static Response myHttpConnection(String content, String url, String aus){
        HttpURLConnection urlConnection = null;
        String result = "N/A";
        int statusCode = 900;
        try {
            urlConnection = (HttpURLConnection) ((new URL(url).openConnection()));
            if(content != null){
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
            }
            else {
                urlConnection.setDoOutput(false);
                urlConnection.setRequestMethod("GET");
            }
         //   urlConnection.setRequestProperty("Content-Type", "application/json");
         //  urlConnection.setRequestProperty("Accept", "application/json");
            if(aus != null){
                urlConnection.setRequestProperty("Authorization", "Basic " + aus);
            }
            urlConnection.setConnectTimeout(6000);
            urlConnection.connect();
            if(content != null){
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(content);
                writer.close();
            }

            try {
                statusCode = urlConnection.getResponseCode();
            } catch (IOException e) {
                statusCode = urlConnection.getResponseCode();
            }

            if (statusCode < 400 ) {
                InputStream is = urlConnection.getInputStream();
                if(is != null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedReader.close();
                    result = sb.toString();
                }
            }
        } catch (Exception e) {
            result = e.toString();

        }
        finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return new Response(result, statusCode);
    }
}
