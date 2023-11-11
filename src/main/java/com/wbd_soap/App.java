package com.wbd_soap;

import com.wbd_soap.model.Database;
import com.wbd_soap.service.ReferenceService;

import javax.xml.ws.Endpoint;
import java.sql.SQLOutput;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Server is listening at http://localhost:8001/api/reference");
        Endpoint.publish("http://localhost:8001/api/reference", new ReferenceService());
}
}
