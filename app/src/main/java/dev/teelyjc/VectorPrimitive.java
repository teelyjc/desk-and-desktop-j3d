package dev.teelyjc;

import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

public class VectorPrimitive {
    private String name;

    private Vector3f vector3f;
    private Primitive primitive;

    public VectorPrimitive(String name, Vector3f vector3f, Primitive primitive) {
        this.name = name;

        this.vector3f = vector3f;
        this.primitive = primitive;
    }

    public Vector3f getVector3f() {
        return this.vector3f;
    }

    public Primitive getPrimitive() {
        return this.primitive;
    }

    public String getName() {
        return this.name;
    }
}
