package com.wuxiao.rxhttp.cache.diskconverter;


import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class DiskConverter implements IDiskConverter {

    @Override
    public Object load(InputStream source) {
        Object value = null;
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(source);
            value = oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
        } finally {
            close(oin);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(sink);
            oos.writeObject(data);
            oos.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            close(oos);
        }
    }

    public  void close(Closeable close) {
        if (close != null) {
            try {
                closeThrowException(close);
            } catch (IOException ignored) {
            }
        }
    }

    public void closeThrowException(Closeable close) throws IOException {
        if (close != null) {
            close.close();
        }
    }
}
