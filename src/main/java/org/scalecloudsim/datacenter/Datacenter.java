package org.scalecloudsim.datacenter;

import org.cloudsimplus.core.SimEntity;
import org.scalecloudsim.innerscheduler.InnerScheduler;
import org.scalecloudsim.interscheduler.InterScheduler;
import org.scalecloudsim.statemanager.StateManager;

import java.util.List;
import java.util.Set;

public interface Datacenter extends SimEntity, DatacenterPrice {
    public static final Datacenter NULL = new DatacenterNull();
    Datacenter setStateManager(StateManager stateManager);

    StateManager getStateManager();

    Datacenter addCollaborationId(int collaborationId);

    Datacenter removeCollaborationId(int collaborationId);

    Set<Integer> getCollaborationIds();

    int getHostNum();

    Datacenter setInterScheduler(InterScheduler interScheduler);

    Datacenter setInnerSchedulers(List<InnerScheduler> innerSchedulers);

    List<InnerScheduler> getInnerSchedulers();

    Datacenter setLoadBalance(LoadBalance loadBalance);

    LoadBalance getLoadBalance();

    Datacenter setResourceAllocateSelector(ResourceAllocateSelector resourceAllocateSelector);

    ResourceAllocateSelector getResourceAllocateSelector();

}
