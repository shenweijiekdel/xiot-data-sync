package cn.geekcity.xiot.view;

import cn.geekcity.xiot.domain.Group;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

import java.util.Optional;

public class GroupConverter extends StringConverter<Group> {

    private ChoiceBox<Group> choiceBox;

    public GroupConverter(ChoiceBox<Group> choiceBox) {
        this.choiceBox = choiceBox;
    }

    @Override
    public String toString(Group object) {
        return object.getCode();
    }

    @Override
    public Group fromString(String string) {
        Optional<Group> first = choiceBox.getItems().stream().filter(x -> x.getCode().equals(string)).findFirst();
        return first.orElse(null);
    }
}
