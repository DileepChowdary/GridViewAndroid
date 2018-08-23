package com.dileeptest.model;

import org.json.JSONObject;

public class GridViewModel
{
    String id,owner,secret,server;
    int farm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public GridViewModel(String result)
    {
        try
        {
            JSONObject jsonOblect = new JSONObject(result);
            id = jsonOblect.isNull("id") ? "" : jsonOblect.getString("id");
            owner = jsonOblect.isNull("owner") ? "" : jsonOblect.getString("owner");
            secret = jsonOblect.isNull("secret") ? "" : jsonOblect.getString("secret");
            server = jsonOblect.isNull("id") ? "" : jsonOblect.getString("server");
            farm = jsonOblect.isNull("farm") ? 0 : jsonOblect.getInt("farm");

        }catch (Exception e)
        {
            System.out.println("GridViewModel   "+e);
        }
    }
}
