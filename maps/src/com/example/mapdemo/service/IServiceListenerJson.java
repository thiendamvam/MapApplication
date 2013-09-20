package com.example.mapdemo.service;

import com.example.mapdemo.service.jsonservice.ServiceResponse;


public interface IServiceListenerJson {
    /*
     * Called when a request has been fisnished without canceled.
     */
    public void onCompleted(com.example.mapdemo.service.jsonservice.Service service, ServiceResponse result);

}
