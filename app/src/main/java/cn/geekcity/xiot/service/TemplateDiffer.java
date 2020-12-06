package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.DataDiffer;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.spec.codec.vertx.instance.ActionCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.EventCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.PropertyCodec;
import cn.geekcity.xiot.spec.codec.vertx.template.ActionTemplateCodec;
import cn.geekcity.xiot.spec.codec.vertx.template.EventTemplateCodec;
import cn.geekcity.xiot.spec.codec.vertx.template.PropertyTemplateCodec;
import cn.geekcity.xiot.spec.definition.ServiceDefinition;
import cn.geekcity.xiot.spec.instance.*;
import cn.geekcity.xiot.spec.template.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplateDiffer implements DataDiffer<DeviceTemplate> {

    private static void deviceDiff(DeviceTemplate source, DeviceTemplate target, List<String> messages) {
        if (target == null) {
            messages.add("当前环境不存在");
            return ;
        }

        if (!source.type().toString().equals(target.type().toString())) {
            messages.add("当前环境实例type不相同 ");
            return;
        }

        descriptionDiff(source.description(), target.description(), messages);
        servicesDiff(source.services(), target.services(), messages);
    }

    private static void servicesDiff(List<ServiceTemplate> source, List<ServiceTemplate> target, List<String> messages) {
        for (ServiceTemplate sourceService : source) {

            Optional<ServiceTemplate> first = target.stream().filter(x -> x.iid() == sourceService.iid()).findFirst();
            if (!first.isPresent()) {
                messages.add("service iid " + sourceService.iid() + "不存在");
                continue;
            }

            serviceDiff(sourceService, first.get(), messages);
        }
    }

    private static void serviceDiff(ServiceTemplate source, ServiceTemplate target, List<String> messages) {
        if (!source.type().toString().equals(target.type().toString())) {
            messages.add("service " + source.type().toString() + "存在差异");
            return;
        }

        propertiesDiff(source.properties(), target.properties(), messages);
        actionsDiff(source.actions(), target.actions(), messages);
        eventsDiff(source.events(), target.events(), messages);
    }

    private static void eventsDiff(List<EventTemplate> source, List<EventTemplate> target, List<String> messages) {
        for (EventTemplate sourceEvent : source) {
            Optional<EventTemplate> first = target.stream().filter(x -> x.iid() == sourceEvent.iid()).findFirst();
            if (!first.isPresent()) {
                messages.add("action " + sourceEvent.type().toString() + "不存在");
                continue;
            }

            eventDiff(sourceEvent, first.get(), messages);
        }
    }

    private static void actionsDiff(List<ActionTemplate> source, List<ActionTemplate> target, List<String> messages) {
        for (ActionTemplate sourceAction : source) {
            Optional<ActionTemplate> first = target.stream().filter(x -> x.iid() == sourceAction.iid()).findFirst();
            if (!first.isPresent()) {
                messages.add("action " + sourceAction.type().toString() + "不存在");
                continue;
            }

            actionDiff(sourceAction, first.get(), messages);
        }
    }

    private static void propertiesDiff(List<PropertyTemplate> source, List<PropertyTemplate> target, List<String> messages) {
        for (PropertyTemplate sourceProperty : source) {
            Optional<PropertyTemplate> first = target.stream().filter(x -> x.iid() == sourceProperty.iid()).findFirst();
            if (!first.isPresent()) {
                messages.add("property " + sourceProperty.type().toString() + "不存在");
                continue;
            }

            propertyDiff(sourceProperty, first.get(), messages);
        }
    }

    private static void eventDiff(EventTemplate source, EventTemplate target, List<String> messages) {
        String sourceString = EventTemplateCodec.encode(source).encode();
        String targetString = EventTemplateCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            messages.add("event " + source.type().toString() + " 不同");
        }
    }

    private static void actionDiff(ActionTemplate source, ActionTemplate target, List<String> messages) {
        String sourceString = ActionTemplateCodec.encode(source).encode();
        String targetString = ActionTemplateCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            messages.add("action " + source.type().toString() + " 不同");
        }
    }

    private static void propertyDiff(PropertyTemplate source, PropertyTemplate target, List<String> messages) {
        String sourceString = PropertyTemplateCodec.encode(source).encode();
        String targetString = PropertyTemplateCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            messages.add("property " + source.type().toString() + " 不同");
        }
    }

    private static void descriptionDiff(Map<String, String> source, Map<String, String> target, List<String> messages) {
        for (Map.Entry<String, String> s : source.entrySet()) {
            String t = target.get(s.getKey());

            if (!t.equals(s.getValue())) {
                messages.add("实例定义描述不同");
                return;
            }
        }
    }

    @Override
    public String diff(DeviceTemplate source, DeviceTemplate current) {
        List<String> messages = new ArrayList<>();
        deviceDiff(source, current, messages);
        return String.join("\n", messages);
    }
}
