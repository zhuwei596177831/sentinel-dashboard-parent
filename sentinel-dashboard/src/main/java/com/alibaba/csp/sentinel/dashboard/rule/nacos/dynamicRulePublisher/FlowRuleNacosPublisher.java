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
package com.alibaba.csp.sentinel.dashboard.rule.nacos.dynamicRulePublisher;

import com.alibaba.csp.sentinel.dashboard.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eric Zhao
 * @author zhuweiwei
 * @description 往nacos配置中心推送sentinel控制台配置的流量控制规则配置
 * @since 1.4.0
 */
@Component("flowRuleNacosPublisher")
public class FlowRuleNacosPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(FlowRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<FlowRuleEntity>, String> converter;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
//        configService.publishConfig(app + NacosConfigUtil.FLOW_DATA_ID_POSTFIX, NacosConfigUtil.GROUP_ID, converter.convert(rules));
        boolean flag = configService.publishConfig(app + nacosConfigProperties.getFlowRuleSuffix(), nacosConfigProperties.getGroupId(), converter.convert(rules));
        String name = "失败";
        if (flag) {
            name = "成功";
        }
        logger.info("推送{}流量控制规则" + name + "：\n{}", app,
                JSON.toJSONString(rules.stream().map(FlowRuleEntity::toRule).collect(Collectors.toList()), true));
    }
}
