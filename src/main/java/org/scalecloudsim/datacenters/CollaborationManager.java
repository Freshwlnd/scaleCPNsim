package org.scalecloudsim.datacenters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollaborationManager {
    public Logger LOGGER = LoggerFactory.getLogger(CollaborationManager.class.getSimpleName());

    CollaborationManager addDatacenter(Datacenter datacenter, int collaborationId);

    CollaborationManager addDatacenter(Map<Integer, Set<Datacenter>> datacenters);

    CollaborationManager removeDatacenter(Datacenter datacenter, int collaborationId);

    CollaborationManager removeDatacenter(Datacenter datacenter);

    Set<Datacenter> getOtherDatacenters(Datacenter datacenter, int collaborationId);

    Set<Datacenter> getOtherDatacenters(Datacenter datacenter);

    Set<Datacenter> getDatacenters(int collaborationId);

    Map<Integer, Set<Datacenter>> getCollaborationMap();
}