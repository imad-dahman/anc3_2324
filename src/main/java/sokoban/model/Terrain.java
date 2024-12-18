package sokoban.model;

class Terrain extends Element {

    public Terrain() {
        super(ElementType.terrain);
        this.setPriority(0);
    }

    @Override
    ElementType getElement() {
        return super.elementTypeProperty().get();    }
}
