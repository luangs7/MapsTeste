package br.com.devmaker.mapsteste.dao.webservice;


import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.util.HashMap;

import br.com.devmaker.mapsteste.dao.custom.CustomResquest;
import br.com.devmaker.mapsteste.dao.voley.CallListener;
import br.com.devmaker.mapsteste.dao.voley.GerenicAbstractDaoImp;
import br.com.devmaker.mapsteste.request.BaseRequest;


/**
 * Created by anderson on 20/05/2015.
 */
public class BaseDao extends GerenicAbstractDaoImp {

    public BaseDao(Context context) {
        super(context);
    }



    public void send(CallListener callListener) {
        Gson gson = new Gson();


        String url = serverUrl + "adreess.json";
        CustomResquest request = new CustomResquest(BaseRequest.class, Request.Method.GET, url, null, callListener, callListener);
        addRequest(request);
    }



}
