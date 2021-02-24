package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 朱伟伟
 * @date 2021-02-23 14:03:19
 * @description nacos配置
 */
@Component
@ConfigurationProperties(prefix = "nacos.server")
@PropertySource(value = {"classpath:nacos.properties"})
public class NacosConfigProperties {
    private String ip;
    private String port;
    private String groupId;
    private String username;
    private String password;
    private String flowRuleSuffix;
    private String degradeRuleSuffix;
    private String authorityRuleSuffix;
    private String paramFlowRuleSuffix;
    private String gatewayFlowRuleSuffix;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlowRuleSuffix() {
        return flowRuleSuffix;
    }

    public void setFlowRuleSuffix(String flowRuleSuffix) {
        this.flowRuleSuffix = flowRuleSuffix;
    }

    public String getDegradeRuleSuffix() {
        return degradeRuleSuffix;
    }

    public void setDegradeRuleSuffix(String degradeRuleSuffix) {
        this.degradeRuleSuffix = degradeRuleSuffix;
    }

    public String getAuthorityRuleSuffix() {
        return authorityRuleSuffix;
    }

    public void setAuthorityRuleSuffix(String authorityRuleSuffix) {
        this.authorityRuleSuffix = authorityRuleSuffix;
    }

    public String getParamFlowRuleSuffix() {
        return paramFlowRuleSuffix;
    }

    public void setParamFlowRuleSuffix(String paramFlowRuleSuffix) {
        this.paramFlowRuleSuffix = paramFlowRuleSuffix;
    }

    public String getGatewayFlowRuleSuffix() {
        return gatewayFlowRuleSuffix;
    }

    public void setGatewayFlowRuleSuffix(String gatewayFlowRuleSuffix) {
        this.gatewayFlowRuleSuffix = gatewayFlowRuleSuffix;
    }
}
