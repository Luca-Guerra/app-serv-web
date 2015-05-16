package models;

// Modello rappresentante l'entità Account codificata nel file xml
public abstract class Account{
    private String name;
    private String surname;
    private String username;
    private String password;
    public Account(String name, String surname, String username, String password){
        this.name=name;
        this.surname=surname;
        this.username=username;
        this.password=password;
    }
    
    public abstract String getRole();
    
    public void setName(String name){
        this.name=name;
    }
    
    public void setSurname(String surname){
        this.surname=surname;
    }
    
    public void setUsername(String username){
        this.username=username;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    public String getName(){
        return name;
    }
    
    public String getSurname(){
        return surname;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    
}
