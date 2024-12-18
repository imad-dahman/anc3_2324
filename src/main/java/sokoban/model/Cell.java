package sokoban.model;

import javafx.beans.property.*;

import java.util.TreeSet;

abstract class Cell {

    private final ObjectProperty<TreeSet<Element>> value = new SimpleObjectProperty<>(new TreeSet<>());
    private final BooleanProperty isListChanged = new SimpleBooleanProperty(false);


    Cell()
    {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        set.add(new Terrain());
        value.set(set);
    }

    Boolean isListChanged()
    {
        return isListChanged.get();
    }
   BooleanProperty isListChangedProperty()
   {
       return isListChanged;
   }

   Element getElement()
   {
       return value.get().last();
   }

    Element getCaisse()
    {
        for (Element element : value.getValue())
        {
            if (element.getElement() == ElementType.caisse)
            {
                Caisse caisse = (Caisse) element ;
                return caisse;
            }
        }
        return null;
    }
    boolean isEmpty() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        if(set.size() == 1)
            return true;
        return false;
    }

    boolean isBox() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        if (set.size()>1)
        {
            for (Element element : set)
            {
                if (element.getElement() ==ElementType.caisse)
                    return true;
            }
        }
        if(getElement().getElement() == ElementType.caisse)
            return true;
        return false;
    }
    boolean isMurTraversable() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        if (set.size()>1)
        {
            for (Element element : set)
            {
                if (element.getElement() ==ElementType.murTraversable)
                    return true;
            }
        }
        if(getElement().getElement() == ElementType.murTraversable)
            return true;
        return false;
    }

    boolean isCible() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        if (set.size()>1)
        {
            for (Element element : set)
            {
                if (element.getElement() ==ElementType.cible)
                    return true;
            }
        }
        if(getElement().getElement()== ElementType.cible)
            return true;
        return false;
    }

    boolean hasballAndBox() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        boolean hasBox = false;
        boolean hasCible = false;
        if (set.size()>1)
        {
            for (Element element : set)
            {
                if (element.getElement() == ElementType.caisse) {
                    hasBox = true;
                } else if (element.getElement() == ElementType.cible) {
                    hasCible = true;
                }
            }
        }

        return hasBox&&hasCible ;
    }
    public boolean hasMatchingBoxAndTarget() {
        TreeSet<Element> set = new TreeSet<>(value.getValue());

        Caisse box = null;
        Cible target = null;
        for (Element element : set) {
            if (element.getElement() == ElementType.caisse) {
                box = (Caisse) element;
            } else if (element.getElement() == ElementType.cible) {
                target = (Cible) element;
            }
        }
        return box != null && target != null && box.getId() == target.getId();
    }



   void addElement(Element elementTypeSelected)
   {

       //System.out.println("avant "+value.getValue());
       TreeSet<Element> set = new TreeSet<>(value.getValue());
       set.add(elementTypeSelected);
       value.set(set);
    //   System.out.println("après "+value.getValue());
       isListChanged.set(!isListChanged.get());
   }

   void removeElement(Element element)
   {
        TreeSet<Element> set = new TreeSet<>(value.getValue());
        if (set.size() > 1)
        {
            set.remove(element);
            value.set(set);
            isListChanged.set(!isListChanged.get());
        }

   }

   TreeSet<Element> getValue()
   {
       return value.get();
   }


    public ReadOnlyObjectProperty<TreeSet<Element>> valueProperty() {
        return value;
    }




}
