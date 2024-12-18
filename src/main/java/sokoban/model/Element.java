package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Element implements Comparable<Element> {

    private final ObjectProperty<ElementType> elementType = new SimpleObjectProperty<>();

    private int priority;

    public Element(ElementType elementType) {
        this.elementType.setValue(elementType);
    }

    public ObjectProperty<ElementType> elementTypeProperty()
    {
        return elementType;
    }

    @Override
    public String toString() {
        return elementType.getValue().toString();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    abstract ElementType getElement();


    @Override
    public int compareTo(Element o) {
        return Integer.compare(this.getPriority(),o.getPriority());
    }
}
