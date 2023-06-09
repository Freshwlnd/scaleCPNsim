package org.scalecloudsim.datacenter;

import org.cloudsimplus.core.Factory;
import org.cloudsimplus.core.Simulation;
import org.scalecloudsim.innerscheduler.InnerScheduler;
import org.scalecloudsim.interscheduler.InterScheduler;
import org.scalecloudsim.statemanager.PartitionRangesManager;
import org.scalecloudsim.statemanager.PredictionManager;
import org.scalecloudsim.statemanager.StateManager;
import org.scalecloudsim.statemanager.StateManagerSimple;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitDatacenter {
    private static Simulation scaleCloudSim;
    private static Factory factory;

    public static void initDatacenters(Simulation scaleCloudSim, Factory factory, String filePath) {
        InitDatacenter.scaleCloudSim = scaleCloudSim;
        InitDatacenter.factory = factory;
        JsonObject jsonObject = readJsonFile(filePath);

        CollaborationManager collaborationManager = new CollaborationManagerSimple(scaleCloudSim);
        for (int i = 0; i < jsonObject.getJsonArray("collaborations").size(); i++) {
            JsonObject collaborationJson = jsonObject.getJsonArray("collaborations").getJsonObject(i);
            for (int j = 0; j < collaborationJson.getJsonArray("datacenters").size(); j++) {
                JsonObject datacenterJson = collaborationJson.getJsonArray("datacenters").getJsonObject(j);
                Datacenter datacenter = getDatacenter(datacenterJson);
                collaborationManager.addDatacenter(datacenter, collaborationJson.getInt("id"));
            }
        }
    }

    private static JsonObject readJsonFile(String filePath) {
        JsonObject jsonObject = null;
        try (FileReader fileReader = new FileReader(filePath); JsonReader reader = Json.createReader(fileReader)) {
            jsonObject = reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static Datacenter getDatacenter(JsonObject datacenterJson) {
        Datacenter datacenter = new DatacenterSimple(scaleCloudSim, datacenterJson.getInt("id"));

        List<InnerScheduler> innerSchedulers = getInnerSchedulers(datacenterJson);
        datacenter.setInnerSchedulers(innerSchedulers);

        StateManager stateManager = getStateManager(datacenterJson, innerSchedulers);
        datacenter.setStateManager(stateManager);

        JsonObject interSchedulerJson = datacenterJson.getJsonObject("interScheduler");
        InterScheduler interScheduler = factory.getInterScheduler(interSchedulerJson.getString("type"));
        datacenter.setInterScheduler(interScheduler);

        JsonObject loadBalanceJson = datacenterJson.getJsonObject("loadBalancer");
        LoadBalance loadBalance = factory.getLoadBalance(loadBalanceJson.getString("type"));
        datacenter.setLoadBalance(loadBalance);

        JsonObject resourceAllocateSelectorJson = datacenterJson.getJsonObject("resourceAllocateSelector");
        ResourceAllocateSelector resourceAllocateSelector = factory.getResourceAllocateSelector(resourceAllocateSelectorJson.getString("type"));
        datacenter.setResourceAllocateSelector(resourceAllocateSelector);

        return datacenter;
    }

    private static List<InnerScheduler> getInnerSchedulers(JsonObject datacenterJson) {
        List<InnerScheduler> innerSchedulers = new ArrayList<>();
        for (int k = 0; k < datacenterJson.getJsonArray("innerSchedulers").size(); k++) {
            JsonObject schedulerJson = datacenterJson.getJsonArray("innerSchedulers").getJsonObject(k);
            Map<Integer, Double> partitionDelay = new HashMap<>();
            JsonObject partitionDelayJson = schedulerJson.getJsonObject("partitionDelay");
            for (String id : partitionDelayJson.keySet()) {
                partitionDelay.put(Integer.parseInt(id), partitionDelayJson.getJsonNumber(id).doubleValue());
            }
            InnerScheduler scheduler = factory.getInnerScheduler(schedulerJson.getString("type"), schedulerJson.getInt("id"), partitionDelay);
            innerSchedulers.add(scheduler);
        }
        return innerSchedulers;
    }

    private static StateManager getStateManager(JsonObject datacenterJson, List<InnerScheduler> innerSchedulers) {
        int hostNum = datacenterJson.getInt("hostNum");
        PartitionRangesManager partitionRangesManager = getPartitionRangesManager(datacenterJson);

        StateManager stateManager = new StateManagerSimple(hostNum, scaleCloudSim, partitionRangesManager, innerSchedulers);

        setPrediction(stateManager, datacenterJson);

        initHostState(stateManager, datacenterJson);

        return stateManager;
    }

    private static PartitionRangesManager getPartitionRangesManager(JsonObject datacenterJson) {
        PartitionRangesManager partitionRangesManager = new PartitionRangesManager();
        for (int k = 0; k < datacenterJson.getJsonArray("partitions").size(); k++) {
            JsonObject partition = datacenterJson.getJsonArray("partitions").getJsonObject(k);
            partitionRangesManager.addRange(partition.getInt("id"), partition.getInt("start_id"), partition.getInt("length"));
        }
        return partitionRangesManager;
    }

    private static void setPrediction(StateManager stateManager, JsonObject datacenterJson) {
        JsonObject predictionJson = datacenterJson.getJsonObject("prediction");
        PredictionManager predictionManager = factory.getPredictionManager(predictionJson.getString("type"));
        stateManager.setPredictionManager(predictionManager);
        boolean isPredict = datacenterJson.getBoolean("isPredict");
        stateManager.setPredictable(isPredict);
    }

    private static void initHostState(StateManager stateManager, JsonObject datacenterJson) {
        for (int k = 0; k < datacenterJson.getJsonArray("hostStates").size(); k++) {
            JsonObject hostStateJson = datacenterJson.getJsonArray("hostStates").getJsonObject(k);
            int cpu = hostStateJson.getInt("cpu");
            int ram = hostStateJson.getInt("ram");
            int storage = hostStateJson.getInt("storage");
            int bw = hostStateJson.getInt("bw");
            int startId = hostStateJson.getInt("startId");
            int length = hostStateJson.getInt("length");
            stateManager.initHostStates(cpu, ram, storage, bw, startId, length);
        }
    }
}
