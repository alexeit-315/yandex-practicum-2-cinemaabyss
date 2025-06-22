package com.example.proxy.filters;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class CustomResponseWrapper extends HttpServletResponseWrapper {
    private int status = SC_OK;

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
        super.setStatus(status);
    }

    @Override
    public void sendError(int sc) throws IOException {
        this.status = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        this.status = sc;
        super.sendError(sc, msg);
    }

    public int getStatus() {
        return status;
    }
}