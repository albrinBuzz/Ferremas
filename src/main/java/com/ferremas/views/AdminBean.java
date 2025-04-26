package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;
import java.util.*;

@Named("adminBean")
@ViewScoped
public class AdminBean {

    //imagen
    private UploadedFile file;


    private List<Inventario> inventarios;
    private Inventario itemSeleccionado;
    private int nuevoStock;
    private boolean mostrarDialogo;
    private Sucursal sucursal;
    private List<Producto>productos;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private SucursalService sucursalService;
    @Autowired
    private HttpSession session;
    private Empleado empleado;
    private Producto productoSeleccionado;
    private List<Categoria>categorias;
    private List<Empleado>empleados;
    private List<Usuario>usuarios;
    private List<Rol>roles;
    private List<Sucursal>sucursals;
    private String categoria;
    private HashMap<String,Categoria>categoriaHashMap;
    private Usuario usuarioSeleccionado;
    private int sucursalId;
    private Set<Rol> rolesSeleccionados;
    private boolean esUpdate;
    // Inyección de otros beans o servicios necesarios
    private String contrasenaInput;

    @PostConstruct
    public void init() {
        try {
            categorias=categoriaService.findAll();
            categoriaHashMap=new HashMap<>();
            categorias.forEach(categoria1 -> categoriaHashMap.put(categoria1.getNombre(),categoria1));

        }catch (Exception e){
            Logger.logInfo(e.getMessage());
        }
        rolesSeleccionados = new HashSet<>();
        productos=productoService.listarTodos();
        empleados=empleadoService.findAll();

        usuarios=usuarioService.findAll();
        roles=rolService.findAll();
        sucursals=sucursalService.findAll();

        usuarios.forEach(usuario -> {
            usuario.getRoles().size();
            if (usuario.getEmpleado() != null && usuario.getEmpleado().getSucursal() != null) {
                usuario.getEmpleado().getSucursal().getNombre();
            }
        });
    }


    public void openNew() {
        this.productoSeleccionado = new Producto();
    }
    public void openNewUsuario(){
        this.usuarioSeleccionado=new Usuario();
    }

    public boolean hasSelectedProducts(){
        return productos != null && !productos.isEmpty();
    }

    private List<Inventario> cargarInventario() {
        return inventarioService.buscarPorSucursal(sucursal.getIdSucursal());
    }

    public void saveProduct(){

        //obtener el nombre de la categoria, y setear la categoria al producto
        var categoria=categoriaHashMap.get(this.categoria);
        productoSeleccionado.setCategoria(categoria);

        if (file!=null){

            try {
                InputStream in = file.getInputStream();
                String nombre = file.getFileName();
                Path rutaBase = Paths.get("src", "main", "webapp", "resources", "images", "product");
                Files.createDirectories(rutaBase); // Asegura que los directorios existen
                productoSeleccionado.setImagen(nombre);
                upload(nombre, rutaBase.toString(), in); // O ajusta `upload` para recibir un Path si es posible
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        productoService.guardar(productoSeleccionado);
        productos=productoService.listarTodos();
    }


    public void deleteProduct(){

        if (productoSeleccionado.getImagen()!=null){
            String nombre = productoSeleccionado.getImagen();
            Path rutaBase = Paths.get("src", "main", "webapp", "resources", "images", "product");
            Path rutaCompleta = rutaBase.resolve(nombre);
            rutaCompleta.toFile().delete();
        }

        productoService.eliminarPorId(productoSeleccionado.getIdProducto());
        productos=productoService.listarTodos();
    }

    public void upload(String fileName, String destination, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(destination + File.separator + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            Logger.logInfo("Archivo creeado en: "+destination + File.separator + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void saveUsuario(){

        usuarioSeleccionado.setRoles(rolesSeleccionados);

        if (sucursalId!=0){
            var sucursal=sucursalService.findById(sucursalId).get();
            Logger.logInfo(sucursal.toString());
            Empleado emp = new Empleado();
            emp.setRutUsuario(usuarioSeleccionado.getRutUsuario());  // debe estar definido
            emp.setSucursal(sucursal);
            emp.setUsuario(usuarioSeleccionado);
            usuarioSeleccionado.setEmpleado(emp);
            Logger.logInfo(emp.toString());
        }

        if (contrasenaInput != null && !contrasenaInput.trim().isEmpty()) {
            usuarioSeleccionado.setContrasena(contrasenaInput);
        }

        Logger.logInfo("Usuario después de guardar: " + usuarioSeleccionado.toString());

        usuarioService.save(usuarioSeleccionado);


        //empleadoService.save(emp);

    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public Inventario getItemSeleccionado() {
        return itemSeleccionado;
    }

    public void setItemSeleccionado(Inventario itemSeleccionado) {
        this.itemSeleccionado = itemSeleccionado;
    }



    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }


    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }



    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }


    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public void setMostrarDialogo(boolean mostrarDialogo) {
        this.mostrarDialogo = mostrarDialogo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        Logger.logInfo("Usuario seleccionado: " + usuarioSeleccionado);
        this.usuarioSeleccionado = usuarioSeleccionado;
        if (usuarioSeleccionado!=null){
            this.rolesSeleccionados = new HashSet<>(usuarioSeleccionado.getRoles());
            esUpdate=true;
        }else {
            this.rolesSeleccionados=new HashSet<>();
        }

    }

    public Set<Rol> getRolesSeleccionados() {
        return rolesSeleccionados;
    }

    public void setRolesSeleccionados(Set<Rol> rolesSeleccionados) {
        Logger.logInfo("Seteando roles");
        this.rolesSeleccionados = rolesSeleccionados;
    }

    public void prepararUsuarioEdicion(Usuario u) {
        this.usuarioSeleccionado = u;
        // Lógica extra: cargar datos relacionados, roles, etc.
        Logger.logInfo("Preparando edición de: " + u.getCorreo());
    }

    public boolean isEsUpdate() {
        return esUpdate;
    }

    public void setEsUpdate(boolean esUpdate) {
        this.esUpdate = esUpdate;
    }

    public String getContrasenaInput() {
        return contrasenaInput;
    }

    public void setContrasenaInput(String contrasenaInput) {
        this.contrasenaInput = contrasenaInput;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public List<Sucursal> getSucursals() {
        return sucursals;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }

    public int getSucursalId() {
        return sucursalId;
    }

    public EmpleadoService getEmpleadoService() {
        return empleadoService;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    // Acción para actualizar el stock del item

}
