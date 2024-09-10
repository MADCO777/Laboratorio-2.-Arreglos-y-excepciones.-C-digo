import java.io.*;
import java.util.*;

// Clase para representar una opción de menú
class MenuOption implements Serializable {
    private String nombre;
    private int cantidadDisponible;
    private String[] platos;

    public MenuOption(String nombre, int cantidadDisponible, String[] platos) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.platos = platos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void decrementarDisponibilidad() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
        } else {
            System.out.println("No hay más disponibilidad para este menú.");
        }
    }

    public void incrementarDisponibilidad() {
        cantidadDisponible++;
    }

    public String[] getPlatos() {
        return platos;
    }

    public void mostrarPlatos() {
        System.out.println("Platos de " + nombre + ": ");
        for (String plato : platos) {
            System.out.println("- " + plato);
        }
    }
}

// Clase para representar a un invitado
class Invitado implements Serializable {
    private String nombre;
    private MenuOption menuAsignado;

    public Invitado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public MenuOption getMenuAsignado() {
        return menuAsignado;
    }

    public void asignarMenu(MenuOption menu) {
        this.menuAsignado = menu;
    }
}

// Sistema de Catering
class SistemaCatering implements Serializable {
    private Map<String, Invitado> invitados = new HashMap<>();
    private Map<String, MenuOption> menus = new HashMap<>();

    // Método para agregar menú
    public void agregarMenu(String nombreMenu, int cantidad, String[] platos) {
        menus.put(nombreMenu, new MenuOption(nombreMenu, cantidad, platos));
    }

    // Método para agregar invitado
    public void agregarInvitado(String nombreInvitado) {
        invitados.put(nombreInvitado, new Invitado(nombreInvitado));
    }

    // Método para asignar menú a invitado
    public void asignarMenuAInvitado(String nombreInvitado, String nombreMenu) {
        Invitado invitado = invitados.get(nombreInvitado);
        MenuOption menu = menus.get(nombreMenu);

        if (invitado != null && menu != null && menu.getCantidadDisponible() > 0) {
            invitado.asignarMenu(menu);
            menu.decrementarDisponibilidad();
            System.out.println("Menú asignado correctamente a " + invitado.getNombre());
        } else {
            System.out.println("No se pudo asignar el menú. Verifica disponibilidad.");
        }
    }

    // Mostrar las opciones seleccionadas por los invitados
    public void mostrarOpcionesSeleccionadas() {
        for (Invitado invitado : invitados.values()) {
            System.out.println("Invitado: " + invitado.getNombre());
            MenuOption menu = invitado.getMenuAsignado();
            if (menu != null) {
                System.out.println("Menú asignado: " + menu.getNombre());
                menu.mostrarPlatos();
            } else {
                System.out.println("No se ha asignado un menú.");
            }
        }
    }

    // Método para actualizar el menú de un invitado
    public void actualizarMenuInvitado(String nombreInvitado, String nuevoMenu) {
        Invitado invitado = invitados.get(nombreInvitado);
        if (invitado != null && invitado.getMenuAsignado() != null) {
            MenuOption menuActual = invitado.getMenuAsignado();
            menuActual.incrementarDisponibilidad(); // Restablece disponibilidad del menú anterior

            MenuOption nuevoMenuOption = menus.get(nuevoMenu);
            if (nuevoMenuOption != null && nuevoMenuOption.getCantidadDisponible() > 0) {
                invitado.asignarMenu(nuevoMenuOption);
                nuevoMenuOption.decrementarDisponibilidad(); // Disminuye la nueva opción
                System.out.println("Menú actualizado correctamente para " + invitado.getNombre());
            } else {
                System.out.println("No hay disponibilidad en el nuevo menú.");
            }
        } else {
            System.out.println("No se encontró al invitado o no tiene menú asignado.");
        }
    }

    // Calcular disponibilidad restante
    public void calcularDisponibilidadRestante() {
        for (MenuOption menu : menus.values()) {
            System.out.println("Disponibilidad del menú " + menu.getNombre() + ": " + menu.getCantidadDisponible());
        }
    }

    // Guardar datos en archivo
    public void guardarDatos(String nombreArchivo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            out.writeObject(this);
            System.out.println("Datos guardados correctamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    // Cargar datos desde archivo
    public static SistemaCatering cargarDatos(String nombreArchivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return (SistemaCatering) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
            return null;
        }
    }

    // Método para ingresar invitados desde consola
    public void ingresarInvitados() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cuántos invitados asistirán? ");
        int numeroDeInvitados = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer de entrada

        for (int i = 0; i < numeroDeInvitados; i++) {
            System.out.print("Ingresa el nombre del invitado " + (i + 1) + ": ");
            String nombreInvitado = scanner.nextLine();
            agregarInvitado(nombreInvitado);
        }
        System.out.println("Invitados ingresados correctamente.");
    }

    // Método para permitir al usuario ingresar opciones de menú desde consola
    public void ingresarOpcionesDeMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cuántas opciones de menú quieres agregar? ");
        int numeroDeMenus = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer

        for (int i = 0; i < numeroDeMenus; i++) {
            System.out.print("Ingresa el nombre del menú: ");
            String nombreMenu = scanner.nextLine();

            System.out.print("Ingresa la cantidad disponible de este menú: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();  // Limpiar buffer

            System.out.print("¿Cuántos platos tendrá este menú? ");
            int numeroDePlatos = scanner.nextInt();
            scanner.nextLine();  // Limpiar buffer

            String[] platos = new String[numeroDePlatos];
            for (int j = 0; j < numeroDePlatos; j++) {
                System.out.print("Ingresa el nombre del plato " + (j + 1) + ": ");
                platos[j] = scanner.nextLine();
            }

            agregarMenu(nombreMenu, cantidad, platos);
        }

        System.out.println("Opciones de menú ingresadas correctamente.");
    }

    // Método para asignar menús a los invitados
    public void asignarMenuAInvitados() {
        Scanner scanner = new Scanner(System.in);

        for (Invitado invitado : invitados.values()) {
            System.out.println("Asignando menú para el invitado: " + invitado.getNombre());
            System.out.print("Ingresa el nombre del menú a asignar: ");
            String nombreMenu = scanner.nextLine();
            asignarMenuAInvitado(invitado.getNombre(), nombreMenu);
        }
    }
}

// Clase principal con el main
public class CateringApp {
    public static void main(String[] args) {
        SistemaCatering sistema = SistemaCatering.cargarDatos("cateringData.dat");
        if (sistema == null) {
            sistema = new SistemaCatering();
        }

        // Ingresar opciones de menú
        sistema.ingresarOpcionesDeMenu();

        // Ingresar invitados
        sistema.ingresarInvitados();

        // Asignar menús a los invitados
        sistema.asignarMenuAInvitados();

        // Mostrar opciones seleccionadas
        sistema.mostrarOpcionesSeleccionadas();

        // Calcular disponibilidad restante
        sistema.calcularDisponibilidadRestante();

        // Guardar los datos al finalizar
        sistema.guardarDatos("cateringData.dat");
    }
}

