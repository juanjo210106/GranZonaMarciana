# Gran Zona Marciana 🚀

**Gran Zona Marciana** es una aplicación móvil nativa para Android diseñada para la gestión integral de un reality show homónimo. El sistema centraliza la interacción entre la producción, los participantes y la audiencia, permitiendo un control total de la dinámica del programa desde un dispositivo móvil.

---

## 📱 Descripción del Proyecto

La aplicación ofrece una solución completa para la administración de usuarios y eventos dentro del reality. Gracias a un sistema de permisos basado en roles, la app adapta su interfaz y funcionalidades según el tipo de usuario que inicie sesión, garantizando seguridad y una experiencia personalizada.

### Características Principales:
* **Gestión Multiperfil:** Diferenciación clara entre Administradores, Concursantes y Espectadores.
* **Seguridad y Permisos:** Restricción de funciones basada en el rol del usuario.
* **Modo Offline:** Persistencia de datos local para garantizar el acceso a la información sin dependencia constante de red.
* **Interfaz Nativa:** Desarrollada siguiendo las guías de Material Design para Android.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java ☕
* **IDE:** Android Studio
* **Base de Datos:** SQLite
* **ORM / Persistencia:** [Android Room](https://developer.android.com/training/data-storage/room)
* **Mínimo SDK:** API 26 (Android 8.0)

---

## 👥 Roles del Sistema

| Rol | Descripción y Funciones |
| :--- | :--- |
| **Administrador** | Gestión de usuarios (CRUD), control de galas, edición de perfiles y supervisión técnica. |
| **Concursante** | Acceso a retos, visualización de estadísticas propias y estado de permanencia. |
| **Espectador** | Consulta de información general, seguimiento de concursantes y participación en votaciones. |

---

## 🏗️ Arquitectura y Datos

La aplicación utiliza la biblioteca **Room** para gestionar la base de datos **SQLite**. Esta arquitectura permite un acceso a datos robusto, utilizando objetos Java (Entities) y DAOs (Data Access Objects) para abstraer la complejidad de las consultas SQL tradicionales.

**Estructura de Persistencia:**
* **Database:** Punto de acceso principal a la conexión de datos persistidos.
* **Entities:** Representación de las tablas (Usuarios, Retos, Votos, etc.).
* **DAOs:** Métodos para acceder, insertar y actualizar los datos.

---

## 🚀 Instalación y Uso

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/gran-zona-marciana.git](https://github.com/tu-usuario/gran-zona-marciana.git)
