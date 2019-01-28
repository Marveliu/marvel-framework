package com.marvel.framework;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

/**
 * DispatcherServlet 测试
 */
public class DispatcherServletTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    private MockServerClient mockServerClient;


    String expected = "You have logged in successfully.";

    @Before
    public void setUp() throws Exception {
        mockServerClient = new MockServerClient("localhost", 5000);
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    public void service() {
        // mockServerClient.
    }

}