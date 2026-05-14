package combos.model;

public class Producto {

    private int id;
    private String nombre;
    private String categoria;
    private String talla;
    private double precio;
    private int stock;
    private String imagen;

    // CONSTRUCTOR VACIO
    public Producto() {
    }

    // CONSTRUCTOR
    public Producto(int id, String nombre, String categoria,
                    String talla, double precio,
                    int stock, String imagen) {

        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talla = talla;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}