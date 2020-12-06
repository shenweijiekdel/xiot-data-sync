package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.DataDiffer;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.spec.codec.vertx.instance.ActionCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.EventCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.PropertyCodec;
import cn.geekcity.xiot.spec.instance.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductDiffer implements DataDiffer<Product> {


    private static void instanceDiff(Product source, Product current, List<String> messages) {
        if (current == null) {
            messages.add("当前环境不存在");
            return ;
        }

        if (!source.getTemplate().equals(current.getTemplate())) {
            messages.add("模板不相同");
        }

        for (Instance value : source.getInstances().values()) {
            Instance currentInstance = current.getInstances().get(value.getVersion());
            if (currentInstance == null) {
                messages.add("目标环境不存在 version " + value.getVersion());
                continue;
            }


            deviceDiff(value.getContent(), currentInstance.getContent(), messages);
        }
    }

    private static void deviceDiff(Device source, Device current, List<String> messages) {
        if (!source.type().toString().equals(current.type().toString())) {
            messages.add("目标环境实例type不相同 ");
            return;
        }

        descriptionDiff(source.description(), current.description(), messages);
        servicesDiff(source.services(), current.services(), messages);
    }

    private static void servicesDiff(Map<Integer, Service> source, Map<Integer, Service> current, List<String> messages) {
        for (Service sourceService : source.values()) {
            Service currentService = current.get(sourceService.iid());
            if (currentService == null) {
                messages.add("service iid " + sourceService.iid() + "不存在");
                continue;
            }

            serviceDiff(sourceService, currentService, messages);
        }
    }

    private static void serviceDiff(Service source, Service current, List<String> messages) {
        if (!source.type().toString().equals(current.type().toString())) {
            messages.add("service " + source.type().toString() + "存在差异");
            return;
        }

        propertiesDiff(source.properties(), current.properties(), messages);
        actionsDiff(source.actions(), current.actions(), messages);
        eventsDiff(source.events(), current.events(), messages);
    }

    private static void eventsDiff(Map<Integer, Event> source, Map<Integer, Event> current, List<String> messages) {
        for (Event sourceEvent : source.values()) {
            Event currentEvent = current.get(sourceEvent.iid());
            if (currentEvent == null) {
                messages.add("action " + sourceEvent.type().toString() + "不存在");
                continue;
            }

            eventDiff(sourceEvent, currentEvent, messages);
        }
    }

    private static void actionsDiff(Map<Integer, Action> source, Map<Integer, Action> current, List<String> messages) {
        for (Action sourceAction : source.values()) {
            Action currentAction = current.get(sourceAction.iid());
            if (currentAction == null) {
                messages.add("action " + sourceAction.type().toString() + "不存在");
                continue;
            }

            actionDiff(sourceAction, currentAction, messages);
        }
    }

    private static void propertiesDiff(Map<Integer, Property> source, Map<Integer, Property> current, List<String> messages) {
        for (Property sourceProperty : source.values()) {
            Property currentProperty = current.get(sourceProperty.iid());
            if (currentProperty == null) {
                messages.add("property " + sourceProperty.type().toString() + "不存在");
                continue;
            }

            propertyDiff(sourceProperty, currentProperty, messages);
        }
    }

    private static void eventDiff(Event source, Event current, List<String> messages) {
        String sourceString = EventCodec.encode(source).encode();
        String currentString = EventCodec.encode(current).encode();
        if (!sourceString.equals(currentString)) {
            messages.add("event " + source.type().toString() + " 不同");
        }
    }

    private static void actionDiff(Action source, Action current, List<String> messages) {
        String sourceString = ActionCodec.encode(source).encode();
        String currentString = ActionCodec.encode(current).encode();
        if (!sourceString.equals(currentString)) {
            messages.add("action " + source.type().toString() + " 不同");
        }
    }

    private static void propertyDiff(Property source, Property current, List<String> messages) {
        String sourceString = PropertyCodec.encode(source).encode();
        String currentString = PropertyCodec.encode(current).encode();
        if (!sourceString.equals(currentString)) {
            messages.add("property " + source.type().toString() + " 不同");
        }
    }

    private static void descriptionDiff(Map<String, String> source, Map<String, String> current, List<String> messages) {
        for (Map.Entry<String, String> s : source.entrySet()) {
            String t = current.get(s.getKey());

            if (!t.equals(s.getValue())) {
                messages.add("实例定义描述不同");
                return;
            }
        }
    }

    @Override
    public String diff(Product source, Product current) {
        List<String> messages = new ArrayList<>();
        instanceDiff(source, current, messages);
        return String.join("\n", messages);
    }
}
