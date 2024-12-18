package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

 class Joueur extends Element {

    private final static IntegerProperty xPosition = new SimpleIntegerProperty();
    private final static  IntegerProperty yPosition = new SimpleIntegerProperty();

    public Joueur() {
        super(ElementType.joueur);
        this.setPriority(1);
    }

    public Joueur(int x,int y) {
        super(ElementType.joueur);
        this.setPriority(1);
        xPosition.set(x);
        yPosition.set(y);
    }

    void setxPosition(int x)
    {
        xPosition.set(x);
    }

    void setyPosition(int y)
    {
        yPosition.set(y);
    }

    int getXPosition()
    {
        return xPosition.get();
    }
    int getyPosition()
    {
        return yPosition.get();
    }

    public IntegerProperty xPositionProperty()
    {
        return xPosition;
    }
    public IntegerProperty yPositionProperty()
    {
        return yPosition;
    }
    public void moveLeft() {
        if (getyPosition() > 0)
            setyPosition(getyPosition() - 1);
        System.out.println(getXPosition() + "  ----" + getyPosition());
    }

    public void moveUp() {
        if (getXPosition() > 0)
            setxPosition(getXPosition() - 1);
        System.out.println(getXPosition() + "  ----" + getyPosition());
    }

    public void moveDown(int line) {
        if (getXPosition() < line-1 )
            setxPosition(getXPosition() + 1);
        System.out.println(getXPosition() + "  ----" + getyPosition());
    }

    public void moveRight(int col) {
        if (getyPosition() < col-1) {
            setyPosition(getyPosition() + 1);
        }
        System.out.println(getXPosition() + "  ----" + getyPosition());
    }


    @Override
    ElementType getElement() {
        return super.elementTypeProperty().get();
    }
}
