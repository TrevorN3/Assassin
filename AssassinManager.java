//Trevor Nichols
//Assassin Manager
//Section: AM; William  Ceriale

import java.util.*;

/**
 * Creates a manager for the game Assassin that
 * manages the "kill ring" and the "graveyard".
 */
public class AssassinManager {
    private AssassinNode killRingFront;
    private AssassinNode graveyardFront;

    // Creates a new Assassin manager for a given list of names.
    // Constructs a kill ring based on the order of the list given.
    // throws an IllegalArgumentException if a empty list is passed.
    public AssassinManager(List<String> names){
        if(names.isEmpty()){
            throw new IllegalArgumentException("The list is empty");
        }
        this.killRingFront = new AssassinNode(names.get(0));
        AssassinNode current = this.killRingFront;
        int size = names.size();
        for(int i = 1; i < size; i++){
                current.next = new AssassinNode(names.get(i));
                current = current.next;
        }
        
        // Creates a circular list by setting the last nodes reference to point
        // back to the front of the list.
        if(size != 1){ 
            current.next = this.killRingFront;
        }
    }
    
    // Prints the current state of the kill ring.
    // Example 
    //     Trevor is Stalking Panda
    //     Panda is Stalking Trevor
    // If there is only one person in the kill ring prints that the person is 
    // stalking themselves.
    // Example
    //     Panda is stalking Panda
    public void printKillRing(){        
        if (this.gameOver()){
             System.out.print("    ");
             System.out.println(this.killRingFront.name + " is stalking "
                                                 + this.killRingFront.name);
        } else{
             System.out.print("    ");
             System.out.println(this.killRingFront.name + " is stalking "
                                             + this.killRingFront.next.name);
             AssassinNode current = this.killRingFront.next;

             while(!current.equals(this.killRingFront)){
                 System.out.print("    ");
                 System.out.println(current.name + " is stalking "
                                                         + current.next.name);
                 current = current.next;
             }
         }
    }
    
    // Prints the graveyard with most recent person killed first
    // followed by the next most recent and so on.
    // Example
    //     Trevor was killed by Panda
    public void printGraveyard(){
        AssassinNode current = this.graveyardFront;        
        while(current != null){
            System.out.print("    ");
            System.out.println(current.name + " was killed by " 
                                                   + current.killer);
            current = current.next;
        }
    }
    
    // Returns true if the given element is in the set
    // in this case returns true if the given person is in the kill ring.
    // Ignores the casing of letters.
    public boolean killRingContains(String name){
        AssassinNode current = this.killRingFront;
        String currentName = "";      
        while (current != null){
            currentName = current.name;
            if (currentName.equalsIgnoreCase(name)){
                return true;
            }
            current = current.next;
            if (current.equals(this.killRingFront)){
                current = null;
            }
        }
        return false;
    }
   
    // Returns true if the given element is in the set
    // in this case returns true if the given person is in the graveyard.
    // Ignores the casing of letters.
    public boolean graveyardContains(String name){
        AssassinNode current = this.graveyardFront;
        String currentName = "";    
        while (current != null){
            currentName = current.name;         
            if (currentName.equalsIgnoreCase(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }
   
    // Returns true if the game is over and false otherwise.
    public boolean gameOver(){
        return this.killRingFront.next == null;
    }
   
    // Returns the name of the winner if the game is over
    // otherwise returns null.
    public String winner(){
        if(this.gameOver()){
            return this.killRingFront.name;
        }
        return null;
    }
    
    // "Kills" the given name by removing them from the kill ring and
    // placing them into the graveyard.
    // Updates kill ring and the graveyard.
    // Ignores casing of letters.
    // throws IllegalStateException if the game is over
    // throws IllegalArgumentException if a name not in the 
    // kill ring is passed.
    public void kill(String name){
        if (this.gameOver()){
            throw new IllegalStateException("The game is over");
        }        
        if(!this.killRingContains(name)){
            throw new IllegalArgumentException
                ("That name is not in the kill ring");
        }   
        AssassinNode current = this.killRingFront;
        AssassinNode endOfKillRing = this.killRingFront.next;
        AssassinNode graveyardTemp = null;
        String tempName = current.name;
        
        // Creates a reference to the back of the list.
        while (!endOfKillRing.next.equals(this.killRingFront)){
            endOfKillRing = endOfKillRing.next;
        }
        String lastName = endOfKillRing.name;
       
        //front case
        if(tempName.equalsIgnoreCase(name)){ 
            if(this.graveyardFront == null){
                this.graveyardFront = current;
                current = current.next;
                this.killRingFront = current;
                endOfKillRing.next = this.killRingFront;
                this.graveyardFront.next = null;
                this.graveyardFront.killer = lastName;
            } else{
                graveyardTemp = this.graveyardFront;
                this.graveyardFront = current;
                current = current.next;
                this.killRingFront = current;
                endOfKillRing.next = this.killRingFront;
                this.graveyardFront.next = graveyardTemp;
                this.graveyardFront.killer = lastName;
            }       
            if(endOfKillRing.equals(this.killRingFront)){
                this.killRingFront.next = null;
            }
       
        //end case
        }else if (lastName.equalsIgnoreCase(name)){         
            while(!current.next.equals(endOfKillRing)){
                current = current.next;
            }          
            if(this.graveyardFront == null){
                this.graveyardFront = endOfKillRing;
                current.next = this.killRingFront;
                this.graveyardFront.next = null;
                this.graveyardFront.killer = current.name; 
            } else{
                graveyardTemp = this.graveyardFront;
                this.graveyardFront = endOfKillRing;
                current.next = this.killRingFront; 
                this.graveyardFront.next = graveyardTemp;
                this.graveyardFront.killer = current.name;
             }         
            if(current.equals(this.killRingFront)){
                this.killRingFront.next = null;
            }
       
        //middle case
        } else{
            tempName = current.next.name;     
            while(!tempName.equalsIgnoreCase(name)){
                current = current.next;
                tempName = current.next.name;
            }
            if(this.graveyardFront == null){
                this.graveyardFront = current.next;
                current.next = this.graveyardFront.next;
                this.graveyardFront.next = null;
                this.graveyardFront.killer = current.name;
            } else{
                graveyardTemp = this.graveyardFront;
                this.graveyardFront = current.next;
                current.next = this.graveyardFront.next;
                this.graveyardFront.next = graveyardTemp;
                this.graveyardFront.killer = current.name;
             }
        }        
    }
     
    /**
     * Each AssassinNode object represents a single node in a linked list
     * for a game of Assassin.
     */
    private static class AssassinNode {
        public final String name;  // this person's name
        public String killer;      // name of who killed this person (null if alive)
        public AssassinNode next;  // next node in the list (null if none)
        
        /**
         * Constructs a new node to store the given name and no next node.
         */
        public AssassinNode(String name) {
            this(name, null);
        }

        /**
         * Constructs a new node to store the given name and a reference
         * to the given next node.
         */
        public AssassinNode(String name, AssassinNode next) {
            this.name = name;
            this.killer = null;
            this.next = next;
        }
    }
    
}