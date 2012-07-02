
public class Keyqueue {
    // head und tail = 0
    private Node head = null, tail = null;
    // totalitems = 0
    private int totalItems = 0;

   
    public Keyqueue() {
        
    }

    // push item in queue
     
    public void push(byte b) {
         // erstelle neues Node objekt
        Node newNode = new Node(b);
        // wenn die queue liste leer ist sind head und tail gleich dem neuen Node objekt
        if (head == null) head = tail = newNode;
         // wenn die Liste nicht leer ist
        else {
            //letztes objekt = nächstes objekt = neues objekt  
            tail.next = newNode;
            // neues objekt  = vorheriges objekt = letztes objekt(tail)
            newNode.prev = tail;
             // tail pointer zeigt auf neues Objekt
            tail = newNode;
        }
        /** increase total items count */ // erhöhe total items um 1
        totalItems += 1;
    }

    
     // entfernt ein item von der Liste und gibt es aus 
    
    public byte pop() {
         // setup objekt wird wiedergegeben
        byte result = 0x00;
        // wenn die liste nicht leer ist
        if (tail != null) {
            // kopiere die daten des letzten items
            result = tail.data;
            /// letztes item wird zu vorheriges item
            tail = tail.prev;
            // wenn die lsite nicht leer ist setzte letztens item auf 0
            if (tail != null) tail.next = null;
             // wenn die liste leer ist dann head = tail = 0
            else head = tail;
             // minimiere total items um -1
            totalItems -= 1; 
        }
         // gebe letztes objekt wieder
        return result;
    }

    /**
     * Removes all instances of an item
     * @param b byte to be removed
     */
    public void removeItems(byte b) {
        /** create temporary node and set it to point to head */
        Node temp = head;
        /** loop while end of list not reached */
        while (temp != null) {
            /** if current pointed to object's data is equal to parameter */
            if (temp.data == b) {
                /** reset object's previous link */
                if (temp.prev != null) temp.prev.next = temp.next;
                /** reset object's next link */
                if (temp.next != null) temp.next.prev = temp.prev;
                /** if object is the head of list then reset head */
                if (temp == head) head = temp.next;
                /** if object is the tail of list then reset tail */
                if (temp == tail) tail = temp.prev;
                /** decrease total items count */
                totalItems -= 1;
            }
            // hole dir das näcshte item aus der liste
            temp = temp.next;
        }
    }

    
     
      // entferne alle elemente
     public void removeAll() {
        head = tail = null;
     }

    
     
     // gebe größe der liste wieder
    public int size() {
        return totalItems;
    }

    
     
     //gebe letztes item aus der liste wieder
    public byte getLastItem() {
        // gebe setup daten wieder
        byte result = 0x00;
         // wenn due liste leer ist kopiere die letzten item daten
        if (tail != null) result = tail.data;
        // gebe daten zurück
        return result;
    }

     //gebe wieder egal ob ein item in der lsite ist oder nicht
    public boolean contains(byte b) {
         // setup daten werden wiedergeben (default = false)
        boolean result = false;
        // erstelle temporaryx node für navifation
        Node temp = head;
         // wiederhole solange bis das Ende der lsite erreicht wurde
        while (temp != null) {
            // wenn daten ncith gefunden gehe raus aus der liste
            if (temp.data == b) { result = true; break; }
            // hole dir näcshtes item aus der liste
            temp = temp.next;
        }
         // gebe result wieder
        return result;
    }

   // daten für die objekt queue
     
    private class Node {
        // key daten
        public  byte data = 0x00;
        // pointers auf null setzten
        public Node prev = null, next = null;

       // kosntrukt mit daten
         
        public Node(byte b)
        {
            // setzte daten = b
            data = b;
        }

        // kopiere konstruktor
      
        public Node(Node n)
        {
            // kopiere daten
            data = n.data;
            // kopiere links
            prev = n.prev;
            next = n.next;
        }
    }
}