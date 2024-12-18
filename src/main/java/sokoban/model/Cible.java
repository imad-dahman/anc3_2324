package sokoban.model;

 public class Cible extends Element{


    private int id;
    public Cible() {
        super(ElementType.cible);
        this.setPriority(3);
    }
     public Cible(int id) {
         super(ElementType.cible);
         this.setPriority(3);
         this.id = id;
     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         this.id = id;
     }

     @Override
    ElementType getElement() {
        return super.elementTypeProperty().get();
    }
}
