package org.cloudsimplus.core;

import org.cloudsimplus.core.events.SimEvent;
import org.cloudsimplus.network.topologies.NetworkTopology;
import org.scalecloudsim.datacenter.CollaborationManager;
import org.scalecloudsim.record.CsvRecord;
import org.scalecloudsim.record.SqlRecord;

import java.util.function.Predicate;

public interface Simulation {
    Simulation NULL=new SimulationNull();
    double clock();

    String clockStr();

    Simulation setClock(double time);

    void addEntity(CloudSimEntity entity);

    SimEvent select(SimEntity dest, Predicate<SimEvent> predicate);

    SimEvent findFirstDeferred(SimEntity dest, Predicate<SimEvent> predicate);

    void send(SimEvent evt);

    CloudInformationService getCis();

    double start();

    void startSync();

    int getNumEntities();

    void setNetworkTopology(NetworkTopology networkTopology);

    NetworkTopology getNetworkTopology();

    void setCollaborationManager(CollaborationManager collaborationManager);

    CollaborationManager getCollaborationManager();

    int getSimulationAccuracy();

    //设置模拟的时间精度，即到小数点后几位，默认是2位，即0.01ms
    void setSimulationAccuracy(int simulationAccuracy);

    SqlRecord getSqlRecord();
}
