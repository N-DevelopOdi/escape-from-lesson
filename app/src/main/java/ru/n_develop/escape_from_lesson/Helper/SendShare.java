package ru.n_develop.escape_from_lesson.Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Dima on 17.03.2016.
 */
public class SendShare extends Thread
{
    private String id;
    private String share;
    private Integer success = 0;
    private InputStream is = null;
    private String result = null;
    private String line = null;

    public void run()
    {
        // создаем лист для отправки запросов
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        // один параметр, если нужно два и более просто добоовляем также
        nameValuePairs.add(new BasicNameValuePair("user", id));
        nameValuePairs.add(new BasicNameValuePair("share", share));

        //  подключаемся к php запросу и отправляем в него id
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://n-develop.16mb.com/escaping/request/user_share.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e)
        {
        }

        // получаем ответ от php запроса в формате json
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e)
        {
        }

        // обрабатываем полученный json
        try
        {
            JSONObject json_data = new JSONObject(result);
            success=(json_data.getInt("success"));
            if (success == 1)
            {

            }
        }
        catch(Exception e)
        {
        }
    }

    // принемаем id при запуске потока
    public void start(String idp, String sharep)
    {
        this.id = idp;
        this.share = sharep;
        this.start();
    }

    public Integer getSuccess()
    {
        return  success;
    }
}
