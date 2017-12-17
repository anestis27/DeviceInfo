package com.example.orion.leitourgika;

/**
 * Created by Orion on 16/1/2015.
 */
public class SimpleProcess {

    private String name,ram;
    private int pid;

    public SimpleProcess(String name, int pid, String ram) {
        this.name = name;
        this.pid = pid;
        this.ram = ram;
    }

    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }

    public String getRam() {
        return ram;
    }
}
