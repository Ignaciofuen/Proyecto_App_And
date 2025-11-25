## Level-Up Gamer 🎧 Proyecto realizado por: Axel Soto - Ignacio Fuenzalida
<img width="366" height="366" alt="levelupgamerimg" src="https://github.com/user-attachments/assets/52e1dc30-06e6-477c-93dc-f886625e6297" />

Este proyecto es una aplicación móvil desarrollada en Android Studio utilizando Kotlin, diseñada para simular una tienda en línea especializada en la venta de periféricos para gamers. Los usuarios pueden registrarse, iniciar sesión, explorar productos y agregarlos a su carrito. Además, incluye una interfaz de administración para la gestión del catálogo de productos.

## :hammer:Características
Para el usuario final 👤:
- `Login`:  Autenticación de usuarios a través de un formulario con correo y contraseña.
- `Registro`: Permite a los nuevos usuarios crear una cuenta en la tienda.
- `Home`: Pantalla inicial donde los usuarios pueden explorar el catálogo de productos.
- `Catálogo de Productos`: Los usuarios pueden navegar por los productos disponibles y agregarlos al carrito de compras.
- `Carrito de Compras`:  Los usuarios pueden gestionar los productos en su carrito (eliminarlos desde ahí) antes de realizar la compra.

Para el usuario administrador 👑:
- `Register y Login`: Idéntico que para usuario, simplemente se valida al momento del Login el dominio del correo que al ser '@admin.cl' reedirige al home de Administrador.
- `Home`: Pantalla donde el administrador dirigirse a la pantalla de gestión de productos.
- `Gestión de Productos`: El administrador puede realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos de la tienda, como agregar nuevos productos o editar los existentes.

## Tecnologías Usadas 💻🛠️⚙️

- `Android Studio`: Entorno de desarrollo integrado (IDE) utilizado para crear la aplicación Android.
- `Kotlin`: Lenguaje de programación principal utilizado en el proyecto para desarrollar la lógica de la aplicación.
- `MVVM (Model-View-ViewModel):`:  El proyecto sigue el patrón de arquitectura MVVM para separar la lógica de la vista, lo que facilita la escalabilidad y el mantenimiento de la aplicación.
- `Model`: Clases que representan los datos y la lógica de negocio (por ejemplo, productos, usuarios).
- `View`: La interfaz de usuario (UI) que interactúa con el ViewModel para mostrar los datos al usuario.
- `ViewModel`: Gestiona los datos y la lógica de presentación, sirviendo de intermediario entre el modelo y la vista.
- `Room Database`: Para el almacenamiento local de productos y datos del carrito de compras.

## Recursos nativos de Android 🤖 
- `🔔 Notificaciones`: Se implementó usando FusedLocationProvider de Google Play Services para obtener la ubicación del usuario de manera eficiente. Se utiliza Geocoder para convertir las coordenadas (latitud y longitud) en una ubicación legible (ciudad y país).
- `📍 Geolocalización`: Se creó un canal de notificaciones (NotificationChannel) para manejar notificaciones locales. Se implementó una función global mostrarNotificacion que se llama desde los composables para mostrar alertas al usuario, como “Nuevo catálogo disponible”. Las notificaciones respetan los permisos de Android 13+ (POST_NOTIFICATIONS) y se integran con la UI de manera segura.
  
Los permisos se solicitan dinámicamente según la versión de Android y se muestra la ubicación en la interfaz.

## Backend desplegado en la nube ☁️
Nuestro backend (Java + Spring Boot) está desplegado en una instancia en la nube.
Actualmente está configurado para iniciar automáticamente cuando se inicia la máquina.

## Integración App ↔ Backend 🔄 
Toda la comunicación se hace utilizando Retrofit

## Tests del Proyecto 📘
El proyecto incluye pruebas unitarias y pruebas instrumentadas para garantizar la calidad del código.

✔️ Pruebas de lógica de negocio (ViewModel)
Framework utilizado: JUnit 4/5, Kotest, MockK.

Se prueban:
- Registro de usuarios
- Login
- Manejo de errores
- Estados del ViewModel
- Validaciones
