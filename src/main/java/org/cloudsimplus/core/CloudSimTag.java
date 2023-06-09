package org.cloudsimplus.core;

import java.util.Set;

public class CloudSimTag {
    public static final int NONE = -1;

    /**
     * Starting constant value for cloud-related tags.
     */
    private static final int BASE = 0;
    public static final int USER_REQUEST_FAIL = BASE - 1;
    public static final int DC_REGISTRATION_REQUEST = BASE + 1;
    public static final int DC_LIST_REQUEST = BASE + 2;
    public static final int USER_REQUEST_SEND = BASE + 3;//需要dc处理接收到的用户请求
    public static final int NEED_SEND_USER_REQUEST = BASE + 4;//USER发送用户请求给dc
    public static final int GROUP_FILTER_DC_BEGIN = BASE + 5;//USER发送用户请求,需要比USER_REQUEST_SEND小
    public static final int GROUP_FILTER_DC_END = BASE + 6;//USER发送用户请求,需要比USER_REQUEST_SEND小
    public static final int ASK_DC_REVIVE_GROUP = BASE + 7;
    public static final int RESPOND_DC_REVIVE_GROUP_ACCEPT = BASE + 8;
    public static final int RESPOND_DC_REVIVE_GROUP_REJECT = BASE + 9;
    public static final int RESPOND_DC_REVIVE_GROUP_GIVE_UP = BASE + 10;
    public static final int RESPOND_DC_REVIVE_GROUP_EMPLOY = BASE + 11;
    public static final int LOAD_BALANCE_SEND = BASE + 12;
    public static final int INNER_SCHEDULE_BEGIN = BASE + 13;
    public static final int INNER_SCHEDULE_END = BASE + 14;
    public static final int ALLOCATE_RESOURCE = BASE + 16;
    public static final int PRE_ALLOCATE_RESOURCE = BASE + 17;//需要在ALLOCATE_RESOURCE之后
    public static final int UPDATE_HOST_STATE = BASE + 18;
    public static final int END_INSTANCE_RUN = BASE + 19;
    private final int priority;

    public static final Set<Integer> UNIQUE_TAG = Set.of(LOAD_BALANCE_SEND, PRE_ALLOCATE_RESOURCE, UPDATE_HOST_STATE);

    public int priority() {
        return priority;
    }

    public static String tagToString(int tag) {
        return switch (tag) {
            case USER_REQUEST_FAIL -> "USER_REQUEST_FAIL";
            case DC_REGISTRATION_REQUEST -> "DC_REGISTRATION_REQUEST";
            case DC_LIST_REQUEST -> "DC_LIST_REQUEST";
            case USER_REQUEST_SEND -> "USER_REQUEST_SEND";
            case NEED_SEND_USER_REQUEST -> "NEED_SEND_USER_REQUEST";
            case GROUP_FILTER_DC_BEGIN -> "GROUP_FILTER_DC_BEGIN";
            case GROUP_FILTER_DC_END -> "GROUP_FILTER_DC_END";
            case ASK_DC_REVIVE_GROUP -> "ASK_DC_REVIVE_GROUP";
            case RESPOND_DC_REVIVE_GROUP_ACCEPT -> "RESPOND_DC_REVIVE_GROUP_ACCEPT";
            case RESPOND_DC_REVIVE_GROUP_REJECT -> "RESPOND_DC_REVIVE_GROUP_REJECT";
            case RESPOND_DC_REVIVE_GROUP_GIVE_UP -> "RESPOND_DC_REVIVE_GROUP_GIVE_UP";
            case RESPOND_DC_REVIVE_GROUP_EMPLOY -> "RESPOND_DC_REVIVE_GROUP_EMPLOY";
            case LOAD_BALANCE_SEND -> "LOAD_BALANCE_SEND";
            case INNER_SCHEDULE_BEGIN -> "INNER_SCHEDULE_BEGIN";
            case INNER_SCHEDULE_END -> "INNER_SCHEDULE_END";
            case PRE_ALLOCATE_RESOURCE -> "PRE_ALLOCATE_RESOURCE";
            case ALLOCATE_RESOURCE -> "ALLOCATE_RESOURCE";
            case UPDATE_HOST_STATE -> "UPDATE_HOST_STATE";
            case END_INSTANCE_RUN -> "END_INSTANCE_RUN";
            default -> "UNKNOWN";
        };
    }

    CloudSimTag() {
        this.priority = 0;
    }
}
