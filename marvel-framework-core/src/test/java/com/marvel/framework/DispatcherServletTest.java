package com.marvel.framework;

import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class DispatcherServletTest {

    private MockServerClient mockServer;
    String expected = "You have logged in successfully.";

    @Before
    public void setUp() throws Exception {
        mockServer = new MockServerClient("localhost",5000);
    }


    @Test
    public void init() {
        DispatcherServlet test = new DispatcherServlet();
        try {
            test.init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void service() {
        DispatcherServlet test = new DispatcherServlet();

        mockServer
                .when(
                request()
                        .withPath("/hello/John")
                        .withMethod("POST")
                        // .withHeader(new Header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN))
                        // .withQueryStringParameter(new Parameter("my-token", "12345"))
                        .withBody("username=foo&password=123456")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(expected)
        );


    }
}