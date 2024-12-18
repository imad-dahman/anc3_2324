package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Mushroom extends Element{

    private IntegerProperty x = new SimpleIntegerProperty();
    private IntegerProperty y = new SimpleIntegerProperty();



    public Mushroom() {
        super(ElementType.mushroom);
        this.setPriority(2);
    }

    public Mushroom(int x , int y) {
        super(ElementType.mushroom);
        this.setPriority(2);
        setX(x);
        setY(y);
    }

    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }

    @Override
    ElementType getElement() {
        return super.elementTypeProperty().get();
    }
}
