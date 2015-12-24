package com.bridgei2i.common.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ResponseHeaderFilter
    implements Filter
{

    public ResponseHeaderFilter()
    {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse)res;
        String headerName;
        for(Enumeration e = fc.getInitParameterNames(); e.hasMoreElements(); response.addHeader(headerName, fc.getInitParameter(headerName)))
            headerName = (String)e.nextElement();

        chain.doFilter(req, response);
    }

    public void init(FilterConfig filterConfig)
    {
        fc = filterConfig;
    }

    public void destroy()
    {
        fc = null;
    }

    FilterConfig fc;
}