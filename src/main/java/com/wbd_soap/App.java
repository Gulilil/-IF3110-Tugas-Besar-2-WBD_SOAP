package com.wbd_soap;

import com.wbd_soap.model.Database;
import com.wbd_soap.service.ReferenceService;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println("Server is listening at http://localhost:8001/ws/reference");
            Endpoint.publish("http://localhost:8001/ws/reference", new ReferenceService());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed starting the server");
        }

    }
}
