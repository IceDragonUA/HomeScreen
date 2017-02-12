package com.example.icedr.homescreendemo.dao;

import com.example.icedr.homescreendemo.model.Asset;
import com.example.icedr.homescreendemo.model.AssetType;
import com.example.icedr.homescreendemo.model.AssetsWrapper;
import com.example.icedr.homescreendemo.model.Client;
import com.example.icedr.homescreendemo.model.ClientsWrapper;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.model.ProjectsWrapper;
import com.example.icedr.homescreendemo.network.IDataLoadingResult;
import com.example.icedr.homescreendemo.network.RestApi;
import com.example.icedr.homescreendemo.network.RestConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DataDao {

    private static DataDao instance;

    private final RestApi restApi;

    public static DataDao getInstance() {
        if (instance == null) {
            instance = new DataDao();
        }
        return instance;
    }

    private DataDao() {
        restApi = RestConfig.create(RestApi.class);
    }

    public void getProjects(final IDataLoadingResult<List<Project>> projectListCallback) {
        Call<ProjectsWrapper> call = restApi.getProjectData();
        call.enqueue(new Callback<ProjectsWrapper>() {
            @Override
            public void onResponse(Response<ProjectsWrapper> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ProjectsWrapper projectsWrapper = response.body();
                    projectListCallback.onResult(projectsWrapper.getProjectList());
                } else {
                    projectListCallback.onFailure(new Exception("Unsuccessful data loading. Failure code [" + response.message() + "]"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                projectListCallback.onFailure(t);
            }
        });
    }

    public void getClientById(final int clientId, final IDataLoadingResult<Client> clientListCallback){
        Call<ClientsWrapper> call = restApi.getClientData();
        call.enqueue(new Callback<ClientsWrapper>() {
            @Override
            public void onResponse(Response<ClientsWrapper> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ClientsWrapper clientsWrapper = response.body();
                    Client clientById = findClientById(clientId, clientsWrapper.getClientList());
                    if (clientById != null) {
                        clientListCallback.onResult(clientById);
                    } else {
                        clientListCallback.onFailure(new Exception("Unsuccessful data loading. Failure code [" + response.message() + "]"));
                    }
                } else {
                    clientListCallback.onFailure(new Exception("Unsuccessful data loading. Failure code [" + response.message() + "]"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                clientListCallback.onFailure(t);
            }
        });

    }

    private Client findClientById(int clientId, List<Client> clientList) {
        for (Client client : clientList) {
           if(clientId == client.getClientId()){
               return client;
            }
        }
        return null;
    }

    public void getAssetsByProjectId (final int projectId, final AssetType assetType , final IDataLoadingResult<List<Asset>> assetListCallback){
        Call<AssetsWrapper> call = restApi.getAssetByProject(projectId);
        call.enqueue(new Callback<AssetsWrapper>() {
            @Override
            public void onResponse(Response<AssetsWrapper> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    List<Asset> assetsList = response.body().getAssetsList();
                    List<Asset> filtrated = filterAssetsByAssetType(assetsList, assetType);
                    assetListCallback.onResult(filtrated);
                } else {
                    assetListCallback.onFailure(new Exception("Unsuccessful data loading. Failure code [" + response.message() + "]"));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private List<Asset> filterAssetsByAssetType(List<Asset> sourceAssetsList, AssetType assetType) {
        List<Asset> res = new ArrayList<>();
        for (Asset asset : sourceAssetsList) {
            if (AssetType.fromString(asset.getAssetType()) == assetType) {
                res.add(asset);
            }
        }
        return res;
    }

}
