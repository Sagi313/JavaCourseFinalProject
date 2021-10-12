package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** This is a Wrapper / Adapter / Decorator type of design pattern */
public interface IHandler {
    public abstract void handle(InputStream fromClient,
                                OutputStream toClient) throws IOException, ClassNotFoundException;
    }
