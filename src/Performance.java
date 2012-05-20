
import java.awt.*;
import java.awt.event.*;

public class Performance {
    public static void main(String[] args){
        taste t1 = new taste();
        t1.start();
    }
       
}

//Threadmethode
class taste extends Thread{
   
    public taste(){   
    }
    public void keyPressed(KeyEvent ev) {}
    public void keyReleased(KeyEvent ev) {}
    public void keyTyped(KeyEvent ev) {}
       
    public void run(){
       
        while(true){
           
            try{   
               
                Thread.sleep(500);
            }
            catch(InterruptedException e){
            System.err.println("Fehler!");
            }
       
        }
    }
}