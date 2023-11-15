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
//            Only if needed
            (new Database()).setupDatabase();
            Endpoint.publish("http://localhost:8001/ws/reference", new ReferenceService());
            System.out.println("Server is listening at http://localhost:8001/ws/reference");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed starting the server");
        }

    }
}
