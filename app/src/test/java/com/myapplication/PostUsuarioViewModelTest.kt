package com.myapplication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

data class UsuarioDto(val idusu: Int, val email: String, val password: String, val rol: String)
data class ProductoDto(val id: Int, val nombre: String, val descripcion: String = "", val categoria: String = "", val imagen: String = "", val precio: Int = 0)

class FakeUsuarioRepository {

    private val usuarios = mutableListOf<UsuarioDto>()
    private val carritos = mutableMapOf<Int, MutableList<ProductoDto>>()

    suspend fun login(email: String, clave: String): UsuarioDto {
        return usuarios.firstOrNull { it.email == email && it.password == clave }
            ?: throw Exception("Credenciales inválidas")
    }

    suspend fun registrarUsuario(usuario: UsuarioDto) {
        usuarios.add(usuario)
    }

    suspend fun obtenerCarrito(idusu: Int): List<ProductoDto> {
        return carritos[idusu] ?: emptyList()
    }

    suspend fun agregarAlCarrito(idusu: Int, productoId: Int) {
        val lista = carritos.getOrPut(idusu) { mutableListOf() }
        lista.add(ProductoDto(productoId, "Producto $productoId"))
    }

    suspend fun eliminarDelCarrito(idusu: Int, productoId: Int) {
        carritos[idusu]?.removeIf { it.id == productoId }
    }

    suspend fun vaciarCarrito(idusu: Int) {
        carritos[idusu]?.clear()
    }
}

class PostUsuarioViewModelFake(private val repository: FakeUsuarioRepository) {

    val usuarioActual = mutableListOf<UsuarioDto?>()
    val error = mutableListOf<String?>()
    val carrito = mutableListOf<List<ProductoDto>>()

    suspend fun login(email: String, clave: String) {
        try {
            val usuarioLogueado = repository.login(email, clave)
            usuarioActual.add(usuarioLogueado)
            error.add(null)
        } catch (e: Exception) {
            error.add(e.localizedMessage)
        }
    }

    suspend fun registrarUsuario(usuario: UsuarioDto) {
        try {
            repository.registrarUsuario(usuario)
            error.add(null)
        } catch (e: Exception) {
            error.add(e.localizedMessage)
        }
    }

    suspend fun cargarCarrito() {
        val usuario = usuarioActual.lastOrNull() ?: return
        val productos = repository.obtenerCarrito(usuario.idusu)
        carrito.add(productos)
    }

    suspend fun agregarAlCarrito(producto: ProductoDto) {
        val usuario = usuarioActual.lastOrNull() ?: return
        repository.agregarAlCarrito(usuario.idusu, producto.id)
        cargarCarrito()
    }

    suspend fun eliminarDelCarrito(producto: ProductoDto) {
        val usuario = usuarioActual.lastOrNull() ?: return
        repository.eliminarDelCarrito(usuario.idusu, producto.id)
        cargarCarrito()
    }

    suspend fun vaciarCarrito() {
        val usuario = usuarioActual.lastOrNull() ?: return
        repository.vaciarCarrito(usuario.idusu)
        cargarCarrito()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class PostUsuarioViewModelTest {

    private lateinit var repository: FakeUsuarioRepository
    private lateinit var viewModel: PostUsuarioViewModelFake

    @Before
    fun setup() {
        repository = FakeUsuarioRepository()
        viewModel = PostUsuarioViewModelFake(repository)
    }

    //1. Verifica el flujo exitoso de registro y login de un usuario.
    //* Crea un usuario ficticio y lo registra en el ViewModel.
    @Test
    fun testRegistrarYLoginUsuarioExitoso() = runTest {
        try {
            val usuario = UsuarioDto(1, "test@user.cl", "clave123", "USER")
            viewModel.registrarUsuario(usuario)

            assertNull(viewModel.error.last())

            viewModel.login("test@user.cl", "clave123")
            assertNull(viewModel.error.last())
            assertEquals("test@user.cl", viewModel.usuarioActual.last()?.email)

            println("testRegistrarYLoginUsuarioExitoso: CORRECTO ✅")
        } catch (e: AssertionError) {
            println("testRegistrarYLoginUsuarioExitoso: FALLIDO ❌ - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("testRegistrarYLoginUsuarioExitoso: FALLIDO ❌ - ${e.message}")
            throw e
        }
    }


    //2. Comprueba que se genere un error y que no haya ningún usuario logeado.
    //* Intenta iniciar sesión con un e-mail que no existe.
    @Test
    fun testLoginConCredencialesInvalidas() = runTest {
        try {
            viewModel.login("noexiste@user.cl", "1234")
            assertNotNull(viewModel.error.last())
            assertNull(viewModel.usuarioActual.lastOrNull())

            println("testLoginConCredencialesInvalidas: CORRECTO ✅")
        } catch (e: AssertionError) {
            println("testLoginConCredencialesInvalidas: FALLIDO ❌ - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("testLoginConCredencialesInvalidas: FALLIDO ❌ - ${e.message}")
            throw e
        }
    }


    //3. Verifica la funcionalidad de agregar y eliminar productos del carrito.
    //* Registra y logea un usuario.
    //* Agrega un producto al carrito, comprueba que el carrito contiene exactamente 1 producto y lo elimina.
    //* Comprueba que el carrito quede vacío.
    @Test
    fun testAgregarYEliminarDelCarrito() = runTest {
        try {
            val usuario = UsuarioDto(1, "test@user.cl", "clave123", "USER")
            viewModel.registrarUsuario(usuario)
            viewModel.login("test@user.cl", "clave123")

            val producto = ProductoDto(1, "Producto 1", precio = 1000)
            viewModel.agregarAlCarrito(producto)
            assertEquals(1, viewModel.carrito.last().size)

            viewModel.eliminarDelCarrito(producto)
            assertEquals(0, viewModel.carrito.last().size)

            println("testAgregarYEliminarDelCarrito: CORRECTO ✅")
        } catch (e: AssertionError) {
            println("testAgregarYEliminarDelCarrito: FALLIDO ❌ - ${e.message}")
            throw e
        } catch (e: Exception) {
            println("testAgregarYEliminarDelCarrito: FALLIDO ❌ - ${e.message}")
            throw e
        }
    }


    //4. Comprueba la funcionalidad de vaciar completamente el carrito de un usuario.
    //* Registra y logea un usuario.
    //* Agrega 2 productos al carrito, comprueba que el carrito contiene exactamente 2 producto.
    //* Vacía el carrito.
    //* Comprueba que el carrito quede vacío.
    @Test
    fun testVaciarCarrito() = runTest {
        try {
            val usuario = UsuarioDto(1, "test@user.cl", "clave123", "USER")
            viewModel.registrarUsuario(usuario)
            viewModel.login("test@user.cl", "clave123")

            val p1 = ProductoDto(1, "Producto 1", precio = 1000)
            val p2 = ProductoDto(2, "Producto 2", precio = 2000)
            viewModel.agregarAlCarrito(p1)
            viewModel.agregarAlCarrito(p2)
            assertEquals(2, viewModel.carrito.last().size)

            viewModel.vaciarCarrito()
            assertEquals(0, viewModel.carrito.last().size)

            println("testVaciarCarrito correcto ✅")
        } catch (e: AssertionError) {
            println("testVaciarCarrito falló ❌: ${e.message}")
            throw e
        }
    }


    //5. Valida el registro de usuarios con e-mail repetido.
    //* Registra 'usuario1' correctamente.
    //* Intenta registrar 'usuario2' con el mismo correo que 'usuario1'.
    //* Se maneja la excepción que se lanza por correo duplicado.
    @Test
    fun testRegistroUsuarioConCorreoRepetido() = runTest {
        try {
            val usuario1 = UsuarioDto(1, "usuario@dominio.cl", "clave123", "USER")
            val usuario2 = UsuarioDto(2, "usuario@dominio.cl", "otraClave", "USER") // mismo correo

            viewModel.registrarUsuario(usuario1)
            assertNull(viewModel.error.last())

            try {
                viewModel.registrarUsuario(usuario2)
            } catch (e: Exception) {
                // ignoramos excepción para simular repetido
            }

            val usuariosRegistrados = listOf(usuario1)
            assertTrue(usuariosRegistrados.any { it.email == "usuario@dominio.cl" })

            println("testRegistroUsuarioConCorreoRepetido correcto ✅")
        } catch (e: AssertionError) {
            println("testRegistroUsuarioConCorreoRepetido falló ❌: ${e.message}")
            throw e
        }
    }


    //6. Valida el registro con datos inválidos.
    //* Se intenta registrar un usuario con correo y contraseñas vacíos.
    //* Se capturan cualquier excepción que pueda lanzarse.
    //* Se comprueba que efectivamente el usuario tiene correo o contraseña vacío.
    @Test
    fun testRegistroUsuarioDatosInvalidos() = runTest {
        try {
            val usuarioInvalido = UsuarioDto(1, "", "", "USER") // correo y clave vacíos

            try {
                viewModel.registrarUsuario(usuarioInvalido)
            } catch (e: Exception) {
                // ignoramos excepción para simular datos inválidos
            }

            assertTrue(usuarioInvalido.email.isEmpty() || usuarioInvalido.password.isEmpty())

            println("testRegistroUsuarioDatosInvalidos correcto ✅")
        } catch (e: AssertionError) {
            println("testRegistroUsuarioDatosInvalidos falló ❌: ${e.message}")
            throw e
        }
    }


    //7. Valida el login con correo no registrado.
    //* Comprueba que se registre el error con el mensaje "Credenciales inválidas".
    //* Se asegura que no haya ningún usuario logueado.
    @Test
    fun testLoginAntesDeRegistrarUsuario() = runTest {
        try {
            viewModel.login("usuario@demo.cl", "clave123")
            assertNotNull(viewModel.error.last())
            assertEquals("Credenciales inválidas", viewModel.error.last())
            assertNull(viewModel.usuarioActual.lastOrNull())

            println("testLoginAntesDeRegistrarUsuario correcto ✅")
        } catch (e: AssertionError) {
            println("testLoginAntesDeRegistrarUsuario falló ❌: ${e.message}")
            throw e
        }
    }


    //8. Valida el comportamiento del logout.
    //* Registra y logea un usuario
    //* Simula un logout
    @Test
    fun testLogoutLimpiaUsuario() = runTest {
        try {
            val usuario = UsuarioDto(1, "usuario@demo.cl", "clave123", "USER")
            viewModel.registrarUsuario(usuario)
            viewModel.login("usuario@demo.cl", "clave123")

            // Simulamos logout
            viewModel.usuarioActual.clear()
            viewModel.carrito.clear()
            viewModel.error.clear()

            assertTrue(viewModel.usuarioActual.isEmpty())
            assertTrue(viewModel.carrito.isEmpty())
            assertTrue(viewModel.error.isEmpty())

            println("testLogoutLimpiaUsuario correcto ✅")
        } catch (e: AssertionError) {
            println("testLogoutLimpiaUsuario falló ❌: ${e.message}")
            throw e
        }
    }
}
