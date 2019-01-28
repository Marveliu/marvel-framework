package com.marvel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Marveliu
 * @since 10/04/2018
 **/
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    /**
     * 覆盖HttpServlet doGet
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd:hh-mm-ss");
        String currenttime = dateFormat.format(new Date());
        System.out.println("currentTime:" + currenttime);
        req.setAttribute("currentTime", currenttime);
        req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req, resp);
    }

}
