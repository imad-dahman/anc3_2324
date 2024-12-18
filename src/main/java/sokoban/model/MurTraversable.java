package sokoban.model;

public class MurTraversable extends Element{


    public MurTraversable() {
        super(ElementType.murTraversable);
        this.setPriority(2);

    }

    @Override
    ElementType getElement() {
        return null;
    }
}
