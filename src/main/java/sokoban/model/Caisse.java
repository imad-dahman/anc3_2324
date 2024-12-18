package sokoban.model;

public class Caisse extends Element{

    private int id;
    private int xPosition;
    private int yPosition;

    public Caisse() {
        super(ElementType.caisse);
        this.setPriority(2);
    }
    public Caisse(int id) {
        super(ElementType.caisse);
        this.setPriority(2);
        this.id = id;
    }
    public Caisse( int xPosition, int yPosition) {
        super(ElementType.caisse);
        this.setPriority(2);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    @Override
    ElementType getElement() {
        return super.elementTypeProperty().get();
    }
    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    // Getter and Setter for yPosition
    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
