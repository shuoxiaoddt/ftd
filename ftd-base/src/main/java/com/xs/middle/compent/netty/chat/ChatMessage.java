package com.xs.middle.compent.netty.chat;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xiaos
 * @date 31/03/2020 15:38
 */
@Data
@ToString
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 6511890358446214653L;

    private String fromIp;

    private Integer fromPort;

    private String toIp;

    private Integer toPort;

    private String content;

    public ChatMessage(String fromIp, Integer fromPort, String toIp, Integer toPort, String content) {
        this.fromIp = fromIp;
        this.fromPort = fromPort;
        this.toIp = toIp;
        this.toPort = toPort;
        this.content = content;
    }
}
