package org.scalecloudsim.user;

import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.scalecloudsim.request.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class UserRequestManagerCsv implements UserRequestManager {
    String fileName = "./src/main/resources/generateRequestParament.csv";
    double[] FromDcPercent;
    private int RequestPerNumMin = -2;
    private int RequestPerNumMax = -2;
    private int RequestTimeIntervalMin = -2;
    private int RequestTimeIntervalMax = -2;
    private int RequestTimes = -2;
    private int RequestGroupNumMin = -2;
    private int RequestGroupNumMax = -2;
    private int GroupInstanceNumMin = -2;
    private int GroupInstanceNumMax = -2;
    private double GroupAccessDelayPercent = -2;
    private int GroupAccessDelayMin = -2;
    private int GroupAccessDelayMax = -2;
    private double GroupEdgePercent = -2;
    private int GroupEdgeIsDirected = -2;
    private double GroupBwPercent = -2;
    private int GroupBwMin = -2;
    private int GroupBwMax = -2;
    private double GroupDelayPercent = -2;
    private int GroupDelayMin = -2;
    private int GroupDelayMax = -2;
    private int InstanceCpuNumMin = -2;
    private int InstanceCpuNumMax = -2;
    private int InstanceRamNumMin = -2;
    private int InstanceRamNumMax = -2;
    private int InstanceStorageNumMin = -2;
    private int InstanceStorageNumMax = -2;
    private int InstanceBwNumMin = -2;
    private int InstanceBwNumMax = -2;
    private int InstanceLifeTimeMin = -2;
    private int InstanceLifeTimeMax = -2;


    private static int instanceId = 0;
    private static int instanceGroupId = 0;
    private static int userRequestId = 0;
    private Random random;
    @Getter
    private double nextSendTime = 0;
    private int sendTimes = 0;

    public UserRequestManagerCsv(String fileName) {
        random = new Random(1);

        this.fileName = fileName;
        // 将CSV文件的路径传递给File对象
        File csvFile = new File(this.fileName);

        // 创建CSVFormat对象，指定CSV文件的格式
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();

        try (CSVParser csvParser = new CSVParser(new FileReader(csvFile), csvFormat)) {
            // 获取CSV文件的迭代器
            // 遍历CSV文件的每一行记录
            for (CSVRecord csvRecord : csvParser) {
                // 从CSV记录中读取特定列的值
                String title = csvRecord.get(0);
                switch (title) {
                    case "FromDcPercent" -> this.FromDcPercent = stringToArray(csvRecord.get(1));
                    case "RequestPerNumMin" -> this.RequestPerNumMin = Integer.parseInt(csvRecord.get(1));
                    case "RequestPerNumMax" -> this.RequestPerNumMax = Integer.parseInt(csvRecord.get(1));
                    case "RequestTimeIntervalMin" -> this.RequestTimeIntervalMin = Integer.parseInt(csvRecord.get(1));
                    case "RequestTimeIntervalMax" -> this.RequestTimeIntervalMax = Integer.parseInt(csvRecord.get(1));
                    case "RequestTimes" -> this.RequestTimes = Integer.parseInt(csvRecord.get(1));
                    case "RequestGroupNumMin" -> this.RequestGroupNumMin = Integer.parseInt(csvRecord.get(1));
                    case "RequestGroupNumMax" -> this.RequestGroupNumMax = Integer.parseInt(csvRecord.get(1));
                    case "GroupInstanceNumMin" -> this.GroupInstanceNumMin = Integer.parseInt(csvRecord.get(1));
                    case "GroupInstanceNumMax" -> this.GroupInstanceNumMax = Integer.parseInt(csvRecord.get(1));
                    case "GroupAccessDelayPercent" ->
                            this.GroupAccessDelayPercent = Double.parseDouble(csvRecord.get(1));
                    case "GroupAccessDelayMin" -> this.GroupAccessDelayMin = Integer.parseInt(csvRecord.get(1));
                    case "GroupAccessDelayMax" -> this.GroupAccessDelayMax = Integer.parseInt(csvRecord.get(1));
                    case "GroupEdgePercent" -> this.GroupEdgePercent = Double.parseDouble(csvRecord.get(1));
                    case "GroupEdgeIsDirected" -> this.GroupEdgeIsDirected = Integer.parseInt(csvRecord.get(1));
                    case "GroupBwPercent" -> this.GroupBwPercent = Double.parseDouble(csvRecord.get(1));
                    case "GroupBwMin" -> this.GroupBwMin = Integer.parseInt(csvRecord.get(1));
                    case "GroupBwMax" -> this.GroupBwMax = Integer.parseInt(csvRecord.get(1));
                    case "GroupDelayPercent" -> this.GroupDelayPercent = Double.parseDouble(csvRecord.get(1));
                    case "GroupDelayMin" -> this.GroupDelayMin = Integer.parseInt(csvRecord.get(1));
                    case "GroupDelayMax" -> this.GroupDelayMax = Integer.parseInt(csvRecord.get(1));
                    case "InstanceCpuNumMin" -> this.InstanceCpuNumMin = Integer.parseInt(csvRecord.get(1));
                    case "InstanceCpuNumMax" -> this.InstanceCpuNumMax = Integer.parseInt(csvRecord.get(1));
                    case "InstanceRamNumMin" -> this.InstanceRamNumMin = Integer.parseInt(csvRecord.get(1));
                    case "InstanceRamNumMax" -> this.InstanceRamNumMax = Integer.parseInt(csvRecord.get(1));
                    case "InstanceStorageNumMin" -> this.InstanceStorageNumMin = Integer.parseInt(csvRecord.get(1));
                    case "InstanceStorageNumMax" -> this.InstanceStorageNumMax = Integer.parseInt(csvRecord.get(1));
                    case "InstanceBwNumMin" -> this.InstanceBwNumMin = Integer.parseInt(csvRecord.get(1));
                    case "InstanceBwNumMax" -> this.InstanceBwNumMax = Integer.parseInt(csvRecord.get(1));
                    case "InstanceLifeTimeMin" -> this.InstanceLifeTimeMin = Integer.parseInt(csvRecord.get(1));
                    case "InstanceLifeTimeMax" -> this.InstanceLifeTimeMax = Integer.parseInt(csvRecord.get(1));
                    default -> {
                        LOGGER.warn("The parameter name {} is not correct, please check the parameter name in the csv file", title);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkVars();
    }

    private Instance generateAnInstance() {
        int cpuNum = random.nextInt(InstanceCpuNumMax - InstanceCpuNumMin + 1) + InstanceCpuNumMin;
        int ramNum = random.nextInt(InstanceRamNumMax - InstanceRamNumMin + 1) + InstanceRamNumMin;
        int storageNum = random.nextInt(InstanceStorageNumMax - InstanceStorageNumMin + 1) + InstanceStorageNumMin;
        int bwNum = random.nextInt(InstanceBwNumMax - InstanceBwNumMin + 1) + InstanceBwNumMin;
        int lifeTime = random.nextInt(InstanceLifeTimeMax - InstanceLifeTimeMin + 1) + InstanceLifeTimeMin;
        return new InstanceSimple(instanceId++, cpuNum, ramNum, storageNum, bwNum, lifeTime);
    }

    private InstanceGroup generateAnInstanceGroup() {
        int instanceNum = random.nextInt(GroupInstanceNumMax - GroupInstanceNumMin + 1) + GroupInstanceNumMin;
        List<Instance> instanceList = new ArrayList<>();
        for (int i = 0; i < instanceNum; i++) {
            instanceList.add(generateAnInstance());
        }
        InstanceGroup instanceGroup = new InstanceGroupSimple(instanceGroupId++, instanceList);
        if (random.nextDouble() < GroupAccessDelayPercent) {
            double accessLatency = random.nextDouble() * (GroupAccessDelayMax - GroupAccessDelayMin) + GroupAccessDelayMin;
            instanceGroup.setAccessLatency(accessLatency);
        }
        return instanceGroup;
    }

    private InstanceGroupGraph generateAnInstanceGroupGraph(List<InstanceGroup> instanceGroups) {
        InstanceGroupGraph instanceGroupGraph = new InstanceGroupGraphSimple(false);
        for (int i = 0; i < instanceGroups.size(); i++) {
            int j;
            if (GroupEdgeIsDirected == 1) {
                j = 0;
            } else {
                j = i + 1;
            }
            for (; j < instanceGroups.size(); j++) {
                if (random.nextDouble() < GroupEdgePercent && j != i) {
                    double bw = random.nextDouble() * (GroupBwMax - GroupBwMin) + GroupBwMin;
                    double delay = random.nextDouble() * (GroupDelayMax - GroupDelayMin) + GroupDelayMin;
                    instanceGroupGraph.addEdge(instanceGroups.get(i), instanceGroups.get(j), bw, delay);
                }
            }
        }
        return instanceGroupGraph;
    }

    UserRequest generateAUserRequest() {
        int groupNum = random.nextInt(RequestGroupNumMax - RequestGroupNumMin + 1) + RequestGroupNumMin;
        List<InstanceGroup> instanceGroups = new ArrayList<>();
        for (int i = 0; i < groupNum; i++) {
            instanceGroups.add(generateAnInstanceGroup());
        }
        InstanceGroupGraph instanceGroupGraph = generateAnInstanceGroupGraph(instanceGroups);
        return new UserRequestSimple(userRequestId++, instanceGroups, instanceGroupGraph);
    }

    @Override
    public Map<Integer, List<UserRequest>> generateOnceUserRequests() {
        Map<Integer, List<UserRequest>> userRequestsMap = new HashMap<>();
        if (sendTimes >= RequestTimes) {
            return null;
        }
        List<UserRequest> userRequests = new ArrayList<>();
        int requestNum = random.nextInt(RequestPerNumMax - RequestPerNumMin + 1) + RequestPerNumMin;
        for (int i = 0; i < requestNum; i++) {
            UserRequest userRequest = generateAUserRequest();
            int belongDatacenterId = -1;
            double randomDouble = random.nextDouble(FromDcPercent[FromDcPercent.length - 1]);
            for (int j = 0; j < FromDcPercent.length; j++) {
                if (randomDouble < FromDcPercent[j]) {
                    belongDatacenterId = j;
                    break;
                }
            }
            userRequest.setBelongDatacenterId(belongDatacenterId);
            userRequest.setSubmitTime(nextSendTime);
            userRequests.add(userRequest);
            if (!userRequestsMap.containsKey(belongDatacenterId)) {
                userRequestsMap.put(belongDatacenterId, new ArrayList<>());
            }
            userRequestsMap.get(belongDatacenterId).add(userRequest);
        }
        nextSendTime = random.nextDouble() * (RequestTimeIntervalMax - RequestTimeIntervalMin) + RequestTimeIntervalMin;
        nextSendTime = BigDecimal.valueOf(nextSendTime).setScale(2, RoundingMode.HALF_UP).doubleValue();
        sendTimes += 1;
        return userRequestsMap;
    }

    @Override
    public List<UserRequest> getUserRequestMap(double startTime, double endTime, int datacenterId) {
        return null;
    }

    private double[] stringToArray(String str) {
        // 去掉开头和结尾的中括号
        str = str.substring(1, str.length() - 1);
        // 以逗号分隔字符串
        String[] strArray = str.split(",");
        double[] doubleArray = new double[strArray.length];
        if (strArray.length == 0) {
            LOGGER.error("The FromDcPercent parameter is not correct, please check the FromDcPercent parameter in {}", this.fileName);
            return doubleArray;
        }
        doubleArray[0] = Double.parseDouble(strArray[0]);
        for (int i = 1; i < strArray.length; i++) {
            doubleArray[i] = doubleArray[i - 1] + Double.parseDouble(strArray[i]);
        }
        return doubleArray;
    }

    public void checkVars() {
        List<String> uninitializedVars = new ArrayList<>();

        if (RequestPerNumMin == -2) {
            uninitializedVars.add("RequestPerNumMin");
        }
        if (RequestPerNumMax == -2) {
            uninitializedVars.add("RequestPerNumMax");
        }
        if (RequestTimeIntervalMin == -2) {
            uninitializedVars.add("RequestTimeIntervalMin");
        }
        if (RequestTimeIntervalMax == -2) {
            uninitializedVars.add("RequestTimeIntervalMax");
        }
        if (RequestTimes == -2) {
            uninitializedVars.add("RequestTimes");
        }
        if (RequestGroupNumMin == -2) {
            uninitializedVars.add("RequestGroupNumMin");
        }
        if (RequestGroupNumMax == -2) {
            uninitializedVars.add("RequestGroupNumMax");
        }
        if (GroupInstanceNumMin == -2) {
            uninitializedVars.add("GroupInstanceNumMin");
        }
        if (GroupInstanceNumMax == -2) {
            uninitializedVars.add("GroupInstanceNumMax");
        }
        if (GroupAccessDelayPercent == -2) {
            uninitializedVars.add("GroupAccessDelayPercent");
        }
        if (GroupAccessDelayMin == -2) {
            uninitializedVars.add("GroupAccessDelayMin");
        }
        if (GroupAccessDelayMax == -2) {
            uninitializedVars.add("GroupAccessDelayMax");
        }
        if (GroupEdgePercent == -2) {
            uninitializedVars.add("GroupEdgePercent");
        }
        if (GroupEdgeIsDirected == -2) {
            uninitializedVars.add("GroupEdgeIsDirected");
        }
        if (GroupBwPercent == -2) {
            uninitializedVars.add("GroupBwPercent");
        }
        if (GroupBwMin == -2) {
            uninitializedVars.add("GroupBwMin");
        }
        if (GroupBwMax == -2) {
            uninitializedVars.add("GroupBwMax");
        }
        if (GroupDelayPercent == -2) {
            uninitializedVars.add("GroupDelayPercent");
        }
        if (GroupDelayMin == -2) {
            uninitializedVars.add("GroupDelayMin");
        }
        if (GroupDelayMax == -2) {
            uninitializedVars.add("GroupDelayMax");
        }
        if (InstanceCpuNumMin == -2) {
            uninitializedVars.add("InstanceCpuNumMin");
        }
        if (InstanceCpuNumMax == -2) {
            uninitializedVars.add("InstanceCpuNumMax");
        }
        if (InstanceRamNumMin == -2) {
            uninitializedVars.add("InstanceRamNumMin");
        }
        if (InstanceRamNumMax == -2) {
            uninitializedVars.add("InstanceRamNumMax");
        }
        if (InstanceStorageNumMin == -2) {
            uninitializedVars.add("InstanceStorageNumMin");
        }
        if (InstanceStorageNumMax == -2) {
            uninitializedVars.add("InstanceStorageNumMax");
        }
        if (InstanceBwNumMin == -2) {
            uninitializedVars.add("InstanceBwNumMin");
        }
        if (InstanceBwNumMax == -2) {
            uninitializedVars.add("InstanceBwNumMax");
        }

        if (uninitializedVars.isEmpty()) {
            LOGGER.info("All variables have been initialized by {}.", this.fileName);
        } else {
            LOGGER.error("The following variables have not been initialized: {}", uninitializedVars);
            throw new RuntimeException("The following variables have not been initialized: " + uninitializedVars);
        }
    }
}
