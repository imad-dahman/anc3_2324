package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

class Mur extends Element {

    private IntegerProperty x = new SimpleIntegerProperty();
    private IntegerProperty y = new SimpleIntegerProperty();
    public Mur() {
        super(ElementType.mur);
        this.setPriority(2);
    }



    public Mur(int x, int y) {
        super(ElementType.mur);
        setX(x);
        setY(y);
        this.setPriority(2);
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
