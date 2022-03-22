package com.ot.springboot.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ServerServiceDemo", targetNamespace = "http://server.webservice.example.com")
public interface ServerServiceDemo {

    @WebMethod
    String emrService(@WebParam String data);
}
