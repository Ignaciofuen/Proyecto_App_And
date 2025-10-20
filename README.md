#Level-Up Gaming
<img width="366" height="366" alt="levelupgamerimg" src="https://github.com/user-attachments/assets/52e1dc30-06e6-477c-93dc-f886625e6297" />
Proyecto de Tienda de Periféricos Gamer

Este proyecto es una aplicación móvil desarrollada en Android Studio utilizando Kotlin, diseñada para simular una tienda en línea especializada en la venta de periféricos para gamers. Los usuarios pueden registrarse, iniciar sesión, explorar productos y agregarlos a su carrito. Además, incluye una interfaz de administración para la gestión del catálogo de productos.

Características
Para el Usuario Final:

Login: Autenticación de usuarios a través de un formulario con correo y contraseña.

Registro: Permite a los nuevos usuarios crear una cuenta en la tienda.

Home: Pantalla inicial donde los usuarios pueden ver las categorías y explorar el catálogo de productos.

Catálogo de Productos: Los usuarios pueden navegar por los productos disponibles, ver sus detalles y agregarlos al carrito de compras.

Carrito de Compras: Los usuarios pueden gestionar los productos en su carrito antes de realizar la compra.

Para el Administrador:

Login: Acceso separado para administradores, similar al proceso de autenticación de usuarios.

Home del Admin: Pantalla donde el administrador puede gestionar los productos del catálogo.

Gestión de Productos: El administrador puede realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos de la tienda, como agregar nuevos productos o editar los existentes.

Tecnologías Usadas

Android Studio: Entorno de desarrollo integrado (IDE) utilizado para crear la aplicación Android.

Kotlin: Lenguaje de programación principal utilizado en el proyecto para desarrollar la lógica de la aplicación.

Firebase:

Firebase Authentication: Para la autenticación de usuarios (login y registro).

Firebase Realtime Database / Firestore: Para la gestión de datos de productos y usuarios.

MVVM (Model-View-ViewModel): El proyecto sigue el patrón de arquitectura MVVM para separar la lógica de la vista, lo que facilita la escalabilidad y el mantenimiento de la aplicación.

Model: Clases que representan los datos y la lógica de negocio (por ejemplo, productos, usuarios).

View: La interfaz de usuario (UI) que interactúa con el ViewModel para mostrar los datos al usuario.

ViewModel: Gestiona los datos y la lógica de presentación, sirviendo de intermediario entre el modelo y la vista.

SQLite / Room Database: Dependiendo de la implementación, se utiliza SQLite o Room para el almacenamiento local de productos y datos del carrito de compras.
