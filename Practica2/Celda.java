public class Celda {
    boolean bomba; // True: Ya hay bomba. False: No hay bomba
    boolean visibilidad;
    int valor;
    boolean bandera;
    int x;
    int y;

    public Celda(){
        this.bomba = false;
        this.visibilidad = false;
        this.valor = 0;
        this.bandera = false;
        this.x = -1;
        this.y = -1;
    }

    boolean tieneBandera(){
        return this.bandera;
    }
    
}

