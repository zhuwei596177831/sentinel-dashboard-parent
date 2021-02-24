/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.entity.rule.*;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @author Eric Zhao
 * @author zhuweiwei
 * @since 1.4.0
 */
@Configuration
public class NacosConfig {

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    /**
     * @author: 朱伟伟
     * @date: 2021-02-24 15:26
     * @description: 以下converter泛型为entity里的rule
     * <p>
     * 解决推送到nacos客户端后 访问控制规则、热点参数规则不生效
     * <p>
     * sentinel客户端的AuthorityRule、ParamFlowRule与AuthorityRuleEntity、ParamFlowRuleEntity属性格式不一致
     **/
    @Bean
    public Converter<List<AuthorityRule>, String> authorityRuleEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<AuthorityRule>> authorityRuleDecoder() {
        return s -> JSON.parseArray(s, AuthorityRule.class);
    }

    @Bean
    public Converter<List<ParamFlowRule>, String> paramFlowRuleEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ParamFlowRule>> paramFlowRuleDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRule.class);
    }

    @Bean
    public Converter<List<GatewayFlowRuleEntity>, String> gatewayFlowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<GatewayFlowRuleEntity>> gatewayFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, GatewayFlowRuleEntity.class);
    }

    /**
     * @author: 朱伟伟
     * @date: 2021-02-23 11:31
     * @description: 配置nacos信息
     **/
    @Bean
    public ConfigService nacosConfigService(NacosConfigProperties nacosConfigProperties) throws Exception {
        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, DashboardConfig.getNacosServer());
//        properties.put(PropertyKeyConst.NAMESPACE, DashboardConfig.getNacosNamespace());
//        properties.put(PropertyKeyConst.USERNAME, DashboardConfig.getNacosUsername());
//        properties.put(PropertyKeyConst.PASSWORD, DashboardConfig.getNacosPassword());

        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getIp() + ":" + nacosConfigProperties.getPort());
        properties.put(PropertyKeyConst.USERNAME, nacosConfigProperties.getUsername());
        properties.put(PropertyKeyConst.PASSWORD, nacosConfigProperties.getPassword());
        return ConfigFactory.createConfigService(properties);
    }
}
