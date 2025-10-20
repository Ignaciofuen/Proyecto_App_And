#Level-Up Gaming
<img width="366" height="366" alt="levelupgamerimg" src="https://github.com/user-attachments/assets/52e1dc30-06e6-477c-93dc-f886625e6297" />

Este proyecto es una aplicación móvil desarrollada en Android Studio utilizando Kotlin, diseñada para simular una tienda en línea especializada en la venta de periféricos para gamers. Los usuarios pueden registrarse, iniciar sesión, explorar productos y agregarlos a su carrito. Además, incluye una interfaz de administración para la gestión del catálogo de productos.

Características
Para el Usuario Final:

Login: Autenticación de usuarios a través de un formulario con correo y contraseña.

Registro: Permite a los nuevos usuarios crear una cuenta en la tienda.

Home: Pantalla inicial donde los usuarios pueden explorar el catálogo de productos.

Catálogo de Productos: Los usuarios pueden navegar por los productos disponibles y agregarlos al carrito de compras.

Carrito de Compras: Los usuarios pueden gestionar los productos en su carrito (eliminarlos desde ahí) antes de realizar la compra.

Panel de Administrador:

Login: Mismo login que para usuario, simplemente se valida el dominio del correo que al ser '@admin.cl' reedirige al home de Administrador.

Home del Admin: Pantalla donde el administrador dirigirse a la pantalla de gestión de productos.

Gestión de Productos: El administrador puede realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos de la tienda, como agregar nuevos productos o editar los existentes.

Tecnologías Usadas

Android Studio: Entorno de desarrollo integrado (IDE) utilizado para crear la aplicación Android.

Kotlin: Lenguaje de programación principal utilizado en el proyecto para desarrollar la lógica de la aplicación.

MVVM (Model-View-ViewModel): El proyecto sigue el patrón de arquitectura MVVM para separar la lógica de la vista, lo que facilita la escalabilidad y el mantenimiento de la aplicación.

Model: Clases que representan los datos y la lógica de negocio (por ejemplo, productos, usuarios).

View: La interfaz de usuario (UI) que interactúa con el ViewModel para mostrar los datos al usuario.

ViewModel: Gestiona los datos y la lógica de presentación, sirviendo de intermediario entre el modelo y la vista.

Room Database: Para el almacenamiento local de productos y datos del carrito de compras.
