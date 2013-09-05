package com.yunat.ccms.core.support.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class DeepClone {

	public static final Object deepClone(Object src) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(src);
            out.close();
            ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            Object clone = in.readObject();
            in.close();
            return (clone);
        } catch (ClassNotFoundException e) {
            throw new InternalError(e.toString());
        } catch (StreamCorruptedException e) {
            throw new InternalError(e.toString());
        } catch (IOException e) {
            throw new InternalError(e.toString());
        }
    }

}
