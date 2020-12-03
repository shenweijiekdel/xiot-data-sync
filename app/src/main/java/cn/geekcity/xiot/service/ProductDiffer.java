package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.spec.codec.vertx.instance.ActionCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.EventCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.PropertyCodec;
import cn.geekcity.xiot.spec.instance.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductDiffer {

    public static List<Product> diff(List<Product> source, List<Product> target) {
        return source.stream().peek(x -> {
            Optional<Product> first = target.stream().filter(x1 -> checkSameProduct(x, x1)).findFirst();
            x.addDiff("");
            if (!first.isPresent()) {
                x.addDiff("目标环境不存在");
                return;
            }

            Product tar = first.get();
            instanceDiff(x, tar);
        }).collect(Collectors.toList());
    }

    private static void instanceDiff(Product source, Product target) {
        if (source.getInstances().size() != target.getInstances().size()) {
            source.addDiff("实例数量不同");
            return;
        }

        for (Instance value : source.getInstances().values()) {
            Instance targetInstance = target.getInstances().get(value.getVersion());
            if (targetInstance == null) {
                source.addDiff("目标环境不存在 version " + value.getVersion());
                continue;
            }


            deviceDiff(value.getContent(), targetInstance.getContent(), source);
        }
    }

    private static void deviceDiff(Device source, Device target, Product product) {
        if (!source.type().toString().equals(target.type().toString())) {
            product.addDiff("目标环境实例type不相同 ");
            return;
        }

        descriptionDiff(source.description(), target.description(), product);
        servicesDiff(source.services(), target.services(), product);
    }

    private static void servicesDiff(Map<Integer, Service> source, Map<Integer, Service> target, Product product) {
        for (Service sourceService : source.values()) {
            Service targetService = target.get(sourceService.iid());
            if (targetService == null) {
                product.addDiff("service iid " + sourceService.iid() + "不存在");
                continue;
            }

            serviceDiff(sourceService, targetService, product);
        }
    }

    private static void serviceDiff(Service source, Service target, Product product) {
        if (!source.type().toString().equals(target.type().toString())) {
            product.addDiff("service " + source.type().toString() + "存在差异");
            return;
        }

        propertiesDiff(source.properties(), target.properties(), product);
        actionsDiff(source.actions(), target.actions(), product);
        eventsDiff(source.events(), target.events(), product);
    }

    private static void eventsDiff(Map<Integer, Event> source, Map<Integer, Event> target, Product product) {
        for (Event sourceEvent : source.values()) {
            Event targetEvent = target.get(sourceEvent.iid());
            if (targetEvent == null) {
                product.addDiff("action " + sourceEvent.type().toString() + "不存在");
                continue;
            }

            eventDiff(sourceEvent, targetEvent, product);
        }
    }

    private static void actionsDiff(Map<Integer, Action> source, Map<Integer, Action> target, Product product) {
        for (Action sourceAction : source.values()) {
            Action targetAction = target.get(sourceAction.iid());
            if (targetAction == null) {
                product.addDiff("action " + sourceAction.type().toString() + "不存在");
                continue;
            }

            actionDiff(sourceAction, targetAction, product);
        }
    }

    private static void propertiesDiff(Map<Integer, Property> source, Map<Integer, Property> target, Product product) {
        for (Property sourceProperty : source.values()) {
            Property targetProperty = target.get(sourceProperty.iid());
            if (targetProperty == null) {
                product.addDiff("property " + sourceProperty.type().toString() + "不存在");
                continue;
            }

            propertyDiff(sourceProperty, targetProperty, product);
        }
    }

    private static void eventDiff(Event source, Event target, Product product) {
        String sourceString = EventCodec.encode(source).encode();
        String targetString = EventCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            product.addDiff("event " + source.type().toString() + " 不同");
        }
    }

    private static void actionDiff(Action source, Action target, Product product) {
        String sourceString = ActionCodec.encode(source).encode();
        String targetString = ActionCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            product.addDiff("action " + source.type().toString() + " 不同");
        }
    }

    private static void propertyDiff(Property source, Property target, Product product) {
        String sourceString = PropertyCodec.encode(source).encode();
        String targetString = PropertyCodec.encode(target).encode();
        if (!sourceString.equals(targetString)) {
            product.addDiff("property " + source.type().toString() + " 不同");
        }
    }

    private static void descriptionDiff(Map<String, String> source, Map<String, String> target, Product product) {
        for (Map.Entry<String, String> s : source.entrySet()) {
            String t = target.get(s.getKey());

            if (!t.equals(s.getValue())) {
                product.addDiff("实例定义描述不同");
                return;
            }
        }
    }

    private static boolean checkSameProduct(Product source, Product target) {
        return source.getModel().equals(target.getModel()) && source.getSpec().equals(target.getSpec()) && source.getGroup().equals(target.getGroup());
    }

}
