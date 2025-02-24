package com.microservice.backand.scrapify_portal.filters;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;


public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] rawData;

    public ResettableStreamHttpServletRequest(HttpServletRequest request) {
        super(request);
        try {
            this.rawData = toByteArray  (request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request input stream", e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ResettableServletInputStream(new ByteArrayInputStream(rawData));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    private static class ResettableServletInputStream extends ServletInputStream {

        private final InputStream stream;

        public ResettableServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
